# Releasing

## Versioning

1. Run the [prepare release script](https://github.com/MiSikora/Laboratory/blob/master/prepare-release.sh) and bump the desirable version part.
2. If there are no errors `git push && git push --tags`.
3. Wait for [the CI server](https://github.com/MiSikora/Ruler/actions) to upload the artifacts.
4. Visit [Sonatype Nexus](https://oss.sonatype.org) and promote the artifacts.
