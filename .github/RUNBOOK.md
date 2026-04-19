# Release Runbook

Publishing `ring-seq` to Maven Central via the **Sonatype Central Portal** (the legacy OSSRH endpoint was sunset on 2025-06-30).

Releases are driven by `sbt-ci-release` + a git tag. The workflow lives at [`.github/workflows/release.yml`](workflows/release.yml).

---

## One-time setup

Do these once per repo. Skip any step that's already done.

### 1. Central Portal account & namespace

1. Log in at <https://central.sonatype.com/>.
2. Verify that the namespace `io.github.scala-tessella` is listed under **Namespaces** and is **verified**.
   - If you have a legacy OSSRH account, Sonatype's migration should have carried it over. If it isn't there, click **Add Namespace**, enter `io.github.scala-tessella`, and follow the GitHub-based verification prompt (it asks you to create a temporary public repo with a given name on the `scala-tessella` org).

### 2. User token (for CI auth)

1. Central Portal → top-right avatar → **View Account** → **Generate User Token**.
2. Copy the two values it shows (shown **once**): a username and a password — these are *not* your login credentials.

### 3. PGP key (for artifact signing)

Maven Central requires signed artifacts. If you already have a GPG key for publishing, reuse it. Otherwise:

```bash
gpg --full-generate-key          # RSA 4096, no expiry, real name + email
gpg --list-secret-keys --keyid-format=long
# Note the long key ID, e.g. ABCDEF0123456789
gpg --keyserver keyserver.ubuntu.com --send-keys ABCDEF0123456789
```

Export the private key, base64-encoded, so it fits in a GitHub secret.

**Linux:**

```bash
gpg --armor --export-secret-keys ABCDEF0123456789 | base64 -w0 > pgp-secret.b64
```

**macOS** (the BSD `base64` has no `-w0`; wrapping newlines must be stripped explicitly):

```bash
gpg --armor --export-secret-keys ABCDEF0123456789 | base64 | tr -d '\n' > pgp-secret.b64
```

**Sanity-check — this step catches the most common CI failure.** Decode the file back; the first line must be the PGP armor header:

```bash
cat pgp-secret.b64 | base64 --decode | head -1
# expected: -----BEGIN PGP PRIVATE KEY BLOCK-----
```

If you see `base64: invalid input` or anything other than the header, the file is malformed — re-export before uploading.

### 4. GitHub Actions secrets

In GitHub → repo → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**, add:

| Secret | Value |
|---|---|
| `SONATYPE_USERNAME` | User token **username** from step 2 |
| `SONATYPE_PASSWORD` | User token **password** from step 2 |
| `PGP_SECRET` | Contents of `pgp-secret.b64` from step 3 |
| `PGP_PASSPHRASE` | The passphrase you set on the key in step 3 |

When pasting `PGP_PASSPHRASE`, be careful to copy the **entire** passphrase — a truncated last character fails with a generic `gpg: signing failed` later in the run, which is surprisingly hard to diagnose. If in doubt, type the passphrase into a scratch file first, verify its length, then paste.

When pasting `PGP_SECRET`, paste the **entire** contents of `pgp-secret.b64` — no quotes, no prefix, no trailing newline.

Delete `pgp-secret.b64` afterwards.

---

## Cutting a release

Releases are triggered by an **annotated tag** starting with `v`. The version comes from `sbt-dynver` (bundled with `sbt-ci-release`), which reads the git tag.

```bash
git checkout main
git pull
git tag -a v0.6.3 -m "Release 0.6.3"
git push origin v0.6.3
```

The `release` workflow will:

1. Check out at the tag.
2. Run `sbt ci-release`, which internally does:
   - `+publishSigned` across all cross-Scala / cross-platform modules (JVM, JS, Native × Scala 3 + 2.13).
   - Uploads a bundle to the Central Portal and, by default, publishes it (no manual "close and release" step).

Watch the run at <https://github.com/scala-tessella/ring-seq/actions/workflows/release.yml>.

After success, artifacts appear on Maven Central within ~15 minutes (sometimes up to a few hours for search index propagation).

## Snapshots (optional)

The workflow currently only publishes on tag push. If you want automatic snapshot publishing on every commit to a branch, add that branch to the `on.push.branches` list in `release.yml` — `sbt-ci-release` will emit a `-SNAPSHOT` version derived from `git describe` whenever the current commit isn't exactly on a tag.

To publish a snapshot on demand, use the **Run workflow** button on the Actions tab (enabled via `workflow_dispatch`).

---

## Verifying a release

```bash
# Check artifact is present (replace version):
curl -I https://repo1.maven.org/maven2/io/github/scala-tessella/ring-seq_3/0.6.3/ring-seq_3-0.6.3.pom
```

Or search <https://central.sonatype.com/artifact/io.github.scala-tessella/ring-seq_3>.

---

## Troubleshooting

- **`401 Unauthorized`** during upload → wrong `SONATYPE_USERNAME` / `SONATYPE_PASSWORD`. Regenerate the user token (it's not your Central Portal login).
- **`Namespace not found / not verified`** → finish the namespace verification in the Central Portal UI.
- **`gpg: signing failed: No secret key`** → `PGP_SECRET` not set, or not base64-encoded correctly. Re-export with `gpg --armor --export-secret-keys KEYID | base64 -w0`.
- **Scala Native step fails on `clang` / `libstdc++`** → the workflow installs them via `apt-get`. If Ubuntu's default version changes, bump the package name in `release.yml`.
- **Tag pushed but workflow didn't run** → confirm the tag matches the filter `v*` and that it was pushed (`git push origin v0.6.3`, not just `git push`).
- **Wrong version published** → `sbt-dynver` uses the tag verbatim minus the leading `v`. Delete the tag locally and remotely (`git tag -d v0.6.3 && git push --delete origin v0.6.3`) and retag. Note: Maven Central coordinates, once published, cannot be overwritten — only superseded by a higher version.

---

## Rolling back

You cannot un-publish a released artifact from Maven Central. If a release is broken:

1. Publish a fixed higher version (`v0.6.4`) ASAP.
2. Optionally, mark the broken version as deprecated on the project website / README.
