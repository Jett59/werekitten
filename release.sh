echo "v0.0.$1" >> README.md
git add .
git commit -m "release v0.0.$1"
git tag -a "v0.0.$1" -m "release v0.0.$1"
git push origin --tags
git push
