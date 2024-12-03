# CIT 594 Group Project: COVID and Property Data Analyzer

## Overview
This project integrates data analysis across multiple datasets, including COVID statistics, property values, and population demographics in Philadelphia. Using Java, the program processes and analyzes CSV and JSON files, providing insights such as vaccination rates, property market values, and custom analyses.

This project builds on the Solo Project, emphasizing modularity, design patterns, and data structure usage.

---

## Features
- **Multi-dataset Analysis**: Combines COVID data, property data, and population data for Philadelphia ZIP Codes.
- **Multi-format Input**: Supports CSV and JSON formats for COVID data.
- **Custom Analysis**: Allows users to define an additional analysis feature involving all three datasets.
- **Logging**: Implements a Singleton Logger to track user inputs and program activity.
- **N-tier Architecture**: Modular design with clear separation of responsibilities.
- **Error Handling**: Ensures invalid input is skipped gracefully without program crashes.

---

## Learning Objectives
This project aims to:
- Utilize N-tier architecture for modular software design.
- Implement Singleton and Strategy design patterns.
- Analyze data using efficient data structures and memoization techniques.
- Enhance teamwork and collaborative coding skills.

---

## File Structure
```
src/
├── edu/upenn/cit594/
│   ├── Main.java                   # Entry point of the program
│   ├── datamanagement/             # Data management classes
│   │   ├── CovidDataReader.java    # Reads COVID data from CSV and JSON
│   │   ├── PropertyDataReader.java # Reads property values data
│   │   ├── PopulationDataReader.java # Reads population data
│   ├── logging/                    # Singleton Logger
│   │   └── Logger.java
│   ├── processor/                  # Processing logic
│   │   └── DataProcessor.java      # Processes user requests
│   ├── ui/                         # User interface tier
│   │   └── ConsoleUI.java          # Manages user interactions
│   └── util/                       # Utility classes
│       ├── CovidRecord.java        # Represents COVID data record
│       ├── PropertyRecord.java     # Represents property data record
│       └── PopulationRecord.java   # Represents population data record
tests/
├── CovidDataReaderTest.java        # Tests for CovidDataReader
├── PropertyDataReaderTest.java     # Tests for PropertyDataReader
├── PopulationDataReaderTest.java   # Tests for PopulationDataReader
└── DataProcessorTest.java          # Tests for DataProcessor
```

---

## Input Data
- **COVID Data**:
  - File types: CSV and JSON
  - Fields: ZIP Code, partially vaccinated, fully vaccinated, hospitalizations, deaths
- **Property Data**:
  - File type: CSV
  - Fields: ZIP Code, market value, livable area
- **Population Data**:
  - File type: CSV
  - Fields: ZIP Code, population

---

## How to Run
The program accepts up to four runtime arguments:

1. `--covid=<file>`: Path to COVID data file (`csv` or `json`).
2. `--properties=<file>`: Path to property data file (`csv`).
3. `--population=<file>`: Path to population data file (`csv`).
4. `--log=<file>`: Path to log file.

### Example Command
```bash
java edu.upenn.cit594.Main --covid=covid-data.json --properties=properties.csv --population=population.csv --log=log.txt
```

### User Options
The program presents a menu for user actions:
1. Display total population for all ZIP Codes.
2. Calculate vaccination rates (partial or full) per capita for a specific date.
3. Compute the average market value of properties in a ZIP Code.
4. Compute the average livable area of properties in a ZIP Code.
5. Calculate total market value per capita for a ZIP Code.
6. Perform custom analysis (team-defined feature).
7. Exit the program.

---

## Design Highlights
### N-tier Architecture
- **UI Tier**: Handles user interactions.
- **Processing Tier**: Manages data analysis and computation.
- **Data Management Tier**: Reads input data from files.
- **Logging Tier**: Tracks activities and user inputs.

### Design Patterns
- **Singleton**: Logger ensures only one instance handles logging throughout the program.
- **Strategy**: Implements strategies for computing averages (e.g., market value vs. livable area).

### Memoization
- Speeds up repetitive computations by caching results for features involving ZIP Code data.

---

## Dependencies
- **Java 11**: Use the Java Development Kit (JDK) 11.
- **JSON.simple Library**: For reading JSON files (`json-simple-1.1.1.jar`).

---

## Testing
Unit tests validate individual components and their integration:
```bash
javac -cp .:json-simple-1.1.1.jar tests/*.java
java -cp .:json-simple-1.1.1.jar tests.CovidDataReaderTest
```

---

## Common Issues
- Ensure all input files exist and have the correct format.
- Verify JSON files use the expected schema.
- Check that ZIP Codes in files are 5 digits for compatibility.

---

## License
This project is distributed for educational purposes. Contact the authors for permissions beyond this scope.

---

## Contact
**Authors**: [William Liu, Prajit Gopal]  
**Email**: [xuanjliu@seas.upenn.edu, gprajit@seas.upenn.edu]  
