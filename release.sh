echo "v0.0.$1" >> RELEASES.md
git add RELEASES.md
git commit -m "release v0.0.$1"
git tag -a "v0.0.$1" -m "release v0.0.$1"
git push origin --tags
git push
