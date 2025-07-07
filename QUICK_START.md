# Quick Setup Guide for Team Members

## First Time Setup (5 minutes)

### 1. Clone the Repository
```bash
git clone <repository-url>
cd xpense
```

### 2. Verify Everything Works
```bash
cd src
javac -cp . app/Main.java
java -cp . app.Main
```
You should see: "Welcome to Xpense - Nkwa Financial Tracker CLI"

### 3. Check Your Assignment
- Open `MODULE_ASSIGNMENTS.md` 
- Find your name and assigned modules
- Read your specific requirements

### 4. Create Your Feature Branch
```bash
git checkout -b feature/your-name-module
# Example: git checkout -b feature/fenteng-data-models
```

### 5. Start Coding
- Navigate to your assigned files in `src/app/modules/`
- Look for `// TODO:` comments - these are your tasks
- Implement step by step
- Test frequently with compilation

### 6. Daily Workflow
```bash
# Pull latest changes
git pull origin main

# Work on your code
# ... coding ...

# Test your code
cd src
javac -cp . app/Main.java
java -cp . app.Main

# Commit your work
git add .
git commit -m "Implement [specific feature]"
git push origin feature/your-branch-name
```

## Important Files to Read First:
1. `MODULE_ASSIGNMENTS.md` - Your specific assignment
2. `CONTRIBUTING.md` - Development guidelines
3. `FUNCTIONAL_REQUIREMENTS.md` - Overall system requirements

## ⚠️ CRITICAL CONSTRAINT:
**NO external packages/libraries allowed!** Only use Java's built-in libraries:
- ✅ `java.util.*` (ArrayList, HashMap, Scanner, etc.)
- ✅ `java.io.*` (File operations)
- ✅ `java.time.*` (Date/Time handling)
- ✅ `java.math.*` (BigDecimal, etc.)
- ❌ NO Maven/Gradle dependencies
- ❌ NO external JARs

## Need Help?
- Check the group chat immediately
- Create a GitHub Issue for bugs/blockers
- Ask teammates for help with integration

## Timeline: 10 Days
- Days 1-3: Basic implementation
- Days 4-6: Core functionality
- Days 7-8: Integration testing
- Days 9-10: Polish and final testing

**Start immediately - everyone can work in parallel!** 🚀
