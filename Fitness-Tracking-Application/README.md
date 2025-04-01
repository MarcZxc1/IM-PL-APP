TO PUSH THIS PROJECT, YOU NEED TO FOLLOW THESE STEPS:

1. git rm --cached src/main/resources/client_secret_*.json
2. echo "src/main/resources/client_secret_*.json" >> .gitignore
3. git add .gitignore
4. git commit -m "Added client secret file to .gitignore"
5. git filter-branch --force --index-filter "git rm --cached --ignore-unmatch src/main/resources/client_secret_*.json" --prune-empty --tag-name-filter cat -- --all
6. git push origin main --force