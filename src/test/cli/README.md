# CLI Menu Testing Suite

This comprehensive testing suite provides tools to recursively test your CLI menu navigation system. It validates all menu paths, input handling, and ensures robust menu functionality.

## 📁 Directory Structure

```
src/test/cli/
├── 📄 CLIMenuTester.java           # Comprehensive automated testing
├── 📄 InteractiveCLIMenuTester.java # Interactive menu simulation
├── 📄 SimpleCLIMenuTester.java     # Quick testing guide
├── 📁 inputs/
│   ├── 📄 test_basic_navigation.txt    # Basic menu flow tests
│   ├── 📄 test_invalid_inputs.txt      # Error handling tests
│   └── 📄 test_complex_navigation.txt  # Complex path tests
├── 📁 scripts/
│   ├── 📄 cli_tester.bat              # Master testing suite
│   └── 📄 run_menu_tester.bat         # Quick launcher
└── 📄 README.md                       # This file
```

## 🚀 Quick Start

### Option 1: Use the Master Testing Suite
```bash
# Navigate to project root
cd "c:\Users\HP\OneDrive - University of Ghana\RB\mySakai\lvl300 2nd sem\DCIT308 DSA II with Prof Sarpong\grp\xpense"

# Run the comprehensive testing suite
src\test\cli\scripts\cli_tester.bat
```

### Option 2: Quick Interactive Testing
```bash
# Run the quick launcher
src\test\cli\scripts\run_menu_tester.bat
```

## 🧪 Testing Tools

### 1. CLIMenuTester.java
**Comprehensive Automated Testing**
- Tests all menu structure validation
- Validates navigation paths recursively
- Checks input validation and error handling
- Tests exit paths from all menus
- Provides detailed test reports

```bash
# Compile and run
javac -cp src src/test/cli/CLIMenuTester.java
java -cp src test.cli.CLIMenuTester
```

### 2. InteractiveCLIMenuTester.java
**Interactive Menu Simulation**
- Real-time menu navigation testing
- Manual test sequence execution
- Navigation history tracking
- Automated test sequence runner

```bash
# Compile and run
javac -cp src src/test/cli/InteractiveCLIMenuTester.java
java -cp src test.cli.InteractiveCLIMenuTester
```

### 3. SimpleCLIMenuTester.java
**Quick Testing Guide**
- Displays ready-to-use test commands
- Shows all navigation paths to test
- Provides validation checklist
- Quick reference for manual testing

```bash
# Compile and run
javac -cp src src/test/cli/SimpleCLIMenuTester.java
java -cp src test.cli.SimpleCLIMenuTester
```

## 📝 Test Input Files

### Basic Navigation Test
**File:** `inputs/test_basic_navigation.txt`
- Tests: Expenditure → View → Back → Help → Alerts → Back → Exit
- Purpose: Validate basic menu flow

### Invalid Input Test
**File:** `inputs/test_invalid_inputs.txt`
- Tests: Error handling with invalid menu choices
- Purpose: Ensure robust input validation

### Complex Navigation Test
**File:** `inputs/test_complex_navigation.txt`
- Tests: Multiple menu levels and deep navigation
- Purpose: Validate complex user journeys

## 🎯 Test Coverage

The testing suite covers:

✅ **Menu Structure Validation**
- All menu options display correctly
- Menu headers and formatting
- Option numbering and labels

✅ **Navigation Testing**
- Main menu to sub-menu navigation
- Return to main menu functionality
- Deep navigation paths
- Menu loop prevention

✅ **Input Validation**
- Valid input acceptance
- Invalid input rejection
- Error message display
- Edge case handling

✅ **Exit Path Testing**
- Exit from each menu level
- Alternative exit commands
- Graceful application termination

✅ **Error Handling**
- Special character inputs
- Null input handling
- Long input strings
- Numeric overflow inputs

## 📊 Using the Master Testing Suite

The `cli_tester.bat` script provides a comprehensive menu:

```
┌─ TESTING OPTIONS ─────────────────────────────────────────┐
│ 1. Compile and Test Your CLI Application                  │
│ 2. Run Interactive Menu Tester                           │
│ 3. Run Simple CLI Menu Guide                             │
│ 4. Test with Basic Navigation Input                      │
│ 5. Test with Invalid Inputs                              │
│ 6. Test with Complex Navigation                          │
│ 7. Show All Test Files                                   │
│ 8. Clean up compiled files                               │
│ 9. Exit                                                  │
└───────────────────────────────────────────────────────────┘
```

## 🔍 Manual Testing Commands

### Compile Your CLI Application
```bash
javac -cp src src/app/Main.java
java -cp src app.Main
```

### Test with Automated Inputs
```bash
# Basic navigation test
java -cp src app.Main < src/test/cli/inputs/test_basic_navigation.txt

# Invalid input test
java -cp src app.Main < src/test/cli/inputs/test_invalid_inputs.txt

# Complex navigation test
java -cp src app.Main < src/test/cli/inputs/test_complex_navigation.txt
```

## ✅ Validation Checklist

Use this checklist to ensure comprehensive testing:

- [ ] All menu options are displayed correctly
- [ ] Navigation between menus works smoothly
- [ ] Return to main menu functions properly
- [ ] Exit commands terminate the application
- [ ] Invalid inputs are handled gracefully
- [ ] No infinite loops or menu traps exist
- [ ] Help menu is accessible and functional
- [ ] All submenu functions are reachable
- [ ] Error messages are clear and helpful
- [ ] Menu formatting is consistent

## 🛠️ Creating Custom Tests

### Adding New Test Input Files
1. Create a new `.txt` file in `inputs/` folder
2. Add your test sequence (one input per line)
3. Update the testing scripts to include your new test

### Example Custom Test File
```
1
3
9
7
2
3
8
```

### Running Custom Tests
```bash
java -cp src app.Main < src/test/cli/inputs/your_custom_test.txt
```

## 🔧 Troubleshooting

### Common Issues

**Compilation Errors:**
- Ensure all package declarations are correct
- Check that all imports are available
- Verify file paths are correct

**Path Issues:**
- Use absolute paths in batch scripts
- Ensure working directory is project root
- Check file separators for Windows (`\`) vs Unix (`/`)

**Test Failures:**
- Verify your CLI application compiles successfully
- Check that menu options match the test expectations
- Ensure input validation is working correctly

## 📈 Test Results Interpretation

### Automated Test Output
- **Green ✓**: Test passed successfully
- **Red ✗**: Test failed, requires attention
- **Yellow ⚠️**: Warning or partial success

### Success Metrics
- **100% Pass Rate**: All menu paths working correctly
- **90-99% Pass Rate**: Minor issues, review failures
- **<90% Pass Rate**: Significant issues require fixing

## 🚀 Integration with Development Workflow

1. **Development Phase**: Use interactive tester for rapid feedback
2. **Testing Phase**: Run comprehensive automated tests
3. **Pre-commit**: Execute full test suite to ensure stability
4. **Documentation**: Use simple guide for team reference

---

## 📞 Support

If you encounter issues or need help extending the testing suite:

1. Check the troubleshooting section above
2. Review the validation checklist
3. Run the simple guide for quick reference
4. Create custom test files for specific scenarios

**Happy Testing! 🎉**
