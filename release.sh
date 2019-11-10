echo "v0.0.$1" >> RELEASES.md
git add RELEASES.md
git commit -m "release v0.0.$1"
git push
git tag -a "v0.0.$1" -m "release v0.0.$1"
git push origin "v0.0.$1"
