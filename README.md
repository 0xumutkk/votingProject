# Online Voting System

A comprehensive Java-based online voting system with Swing GUI, implementing MVC architecture with CSV file persistence. This system allows administrators to manage elections, candidates, and voters, while enabling secure voting for registered voters.

## 📋 Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Technical Details](#technical-details)
- [Security Features](#security-features)
- [CSV File Formats](#csv-file-formats)
- [Troubleshooting](#troubleshooting)

## ✨ Features

### Administrator Features
- **Candidate Management**: Add, update, and delete candidates
- **Voter Management**: 
  - Manual voter registration
  - Bulk import from CSV (FR-A07)
  - Delete voters
- **Election Control**: Start and stop elections
- **Results View**: View real-time election results sorted by vote count

### Voter Features
- **Secure Authentication**: Login with voter ID and password
- **Vote Casting**: Select and vote for candidates
- **Single Vote Enforcement**: System prevents duplicate voting (FR-C01)
- **Vote Status Display**: See if you have already voted

### Security Features
- **Password Hashing**: SHA-256 encryption for all passwords (NFR-S01)
- **Authentication System**: Separate login for administrators and voters
- **Data Persistence**: All data stored in CSV files

## 🏗️ Architecture

The project follows the **Model-View-Controller (MVC)** architectural pattern:

```
┌─────────────┐
│    View     │  (Swing GUI - LoginPanel, AdminPanel, VotingPanel)
│   (Swing)   │
└──────┬──────┘
       │
┌──────▼──────┐
│ Controller  │  (Business Logic - Authentication, Voting, Admin)
└──────┬──────┘
       │
┌──────▼──────┐
│    Model    │  (Data Entities - Voter, Candidate, VoteRecord, Administrator)
└──────┬──────┘
       │
┌──────▼──────┐
│    Utils    │  (DataManager - CSV I/O, PasswordUtils - Hashing)
└─────────────┘
```

### Package Structure

- **`model`**: Entity classes (Voter, Candidate, VoteRecord, Administrator)
- **`controller`**: Business logic (AuthenticationController, VotingController, AdministratorController, Election)
- **`view`**: GUI components (MainFrame, LoginPanel, AdminPanel, VotingPanel)
- **`utils`**: Utility classes (DataManager, PasswordUtils)

## 📦 Requirements

- **Java**: JDK 8 or higher
- **Operating System**: Windows, macOS, or Linux
- **No External Dependencies**: Uses only standard Java libraries

## 🚀 Installation

1. **Clone or download the project**
   ```bash
   cd principleProject
   ```

2. **Compile the project**
   ```bash
   javac -d out -sourcepath src/main/java src/main/java/**/*.java src/main/java/*.java
   ```

3. **Run the application**
   ```bash
   java -cp out Main
   ```

   Or use your IDE:
   - Open the project in your IDE
   - Run `Main.java`

## 📖 Usage

### Initial Setup

1. **First Launch**
   - The system automatically creates a default administrator account:
     - **Username**: `admin`
     - **Password**: `admin`
   - CSV files are created automatically if they don't exist

### Administrator Login

1. Launch the application
2. Enter credentials:
   - **Username**: `admin`
   - **Password**: `admin`
3. Click "Login"

### Managing Candidates

1. Navigate to **"Manage Candidates"** tab
2. **Add Candidate**:
   - Enter Candidate ID, Name, and Position
   - Click "Add Candidate"
3. **Update Candidate**:
   - Enter Candidate ID and new information
   - Click "Update Candidate"
4. **Delete Candidate**:
   - Enter Candidate ID
   - Click "Delete Candidate"
   - Confirm deletion

### Managing Voters

1. Navigate to **"Manage Voters"** tab

#### Manual Registration
- Enter **Voter ID** and **Password**
- Click **"Register Single Voter"**
- Voter can now login with these credentials

#### Bulk Import (FR-A07)
1. Prepare a CSV file with format:
   ```csv
   voterId,password
   101,student123
   102,student456
   103,student789
   ```
2. Click **"Import Voters from CSV"**
3. Select your CSV file
4. View import summary (successful imports, duplicates skipped)

#### Delete Voter
- Enter **Voter ID**
- Click **"Delete Voter"**
- Confirm deletion

### Starting an Election

1. Navigate to **"Election Control"** tab
2. Click **"Start Election"**
3. Status changes to **ACTIVE**
4. Voters can now cast votes

### Stopping an Election

1. Navigate to **"Election Control"** tab
2. Click **"Stop Election"**
3. Status changes to **CLOSED**
4. No more votes can be cast

### Viewing Results

1. Navigate to **"View Results"** tab
2. Click **"Refresh Results"**
3. View candidates sorted by vote count (highest first)

### Voter Voting

1. **Login**:
   - Enter Voter ID and Password
   - Click "Login"

2. **Cast Vote**:
   - Select a candidate using radio buttons
   - Click **"Vote"**
   - Confirm success message

3. **Vote Status**:
   - After voting, controls are disabled
   - Message displays: "You have already voted. Thank you!"

## 📁 Project Structure

```
principleProject/
├── src/main/java/
│   ├── Main.java                    # Application entry point
│   ├── model/                       # Entity classes
│   │   ├── Voter.java
│   │   ├── Candidate.java
│   │   ├── VoteRecord.java
│   │   └── Administrator.java
│   ├── controller/                  # Business logic
│   │   ├── AuthenticationController.java
│   │   ├── VotingController.java
│   │   ├── AdministratorController.java
│   │   └── Election.java            # Singleton election manager
│   ├── view/                        # GUI components
│   │   ├── MainFrame.java           # Main window with CardLayout
│   │   ├── LoginPanel.java
│   │   ├── AdminPanel.java
│   │   └── VotingPanel.java
│   └── utils/                       # Utility classes
│       ├── DataManager.java         # CSV file I/O
│       └── PasswordUtils.java       # SHA-256 hashing
├── voters.csv                       # Voter data
├── candidates.csv                    # Candidate data
├── votes.csv                        # Vote records
├── administrators.csv                # Administrator accounts
└── README.md                        # This file
```

## 🔧 Technical Details

### Design Patterns

- **Singleton Pattern**: `Election` class ensures single election instance
- **MVC Pattern**: Separation of concerns (Model, View, Controller)
- **CardLayout**: Panel switching in MainFrame

### Data Persistence

- **Format**: CSV files with header rows
- **Location**: Project root directory
- **Synchronization**: All operations immediately save to CSV
- **Data Preservation**: Import operations preserve existing voting records

### Key Classes

#### Model Classes
- **Voter**: `id`, `hasVoted`, `password`
- **Candidate**: `candidateId`, `name`, `position`, `voteCount`
- **VoteRecord**: `voterId`, `candidateId`, `timestamp`
- **Administrator**: `username`, `password`

#### Controller Classes
- **Election**: Manages election status (ACTIVE/CLOSED), calculates tally
- **AuthenticationController**: Handles login for admins and voters
- **VotingController**: Validates and processes votes
- **AdministratorController**: Manages candidates and voters

#### View Classes
- **MainFrame**: Main window with CardLayout for panel switching
- **LoginPanel**: Authentication interface
- **AdminPanel**: Admin management interface with tabs
- **VotingPanel**: Voter voting interface

## 🔒 Security Features

### Password Security (NFR-S01)
- All passwords hashed using **SHA-256** algorithm
- Passwords never stored in plain text
- Hashing performed via `PasswordUtils.hashPassword()`

### Vote Security (FR-C01)
- **Single Vote Rule**: Each voter can vote only once
- `hasVoted` flag prevents duplicate voting
- Validation at multiple layers:
  - UI layer: Disables controls after voting
  - Controller layer: Checks `hasVoted` status
  - Model layer: Persistent flag in CSV

### Authentication
- Separate authentication for administrators and voters
- SHA-256 password comparison
- Secure session management

## 📄 CSV File Formats

### voters.csv
```csv
id,hasVoted,password
101,false,5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8
102,true,6cf615d5bcaac778352a8f1f3360d23f02f34ec182e259897fd6ce485d7870d4
```

### candidates.csv
```csv
candidateId,name,position,voteCount
C001,John Doe,President,15
C002,Jane Smith,Vice President,12
```

### votes.csv
```csv
voterId,candidateId,timestamp
101,C001,2024-01-15 10:30:45
102,C002,2024-01-15 11:15:22
```

### administrators.csv
```csv
username,password
admin,8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
```

### Import CSV Format (for bulk voter import)
```csv
voterId,password
101,student123
102,student456
103,student789
```

## 🐛 Troubleshooting

### Application won't start
- **Check Java version**: Ensure JDK 8+ is installed
- **Check compilation**: Recompile if needed
- **Check CSV files**: Ensure CSV files exist in project root

### Login fails
- **Default admin**: Use `admin` / `admin` for first login
- **Password**: Passwords are case-sensitive
- **CSV format**: Check administrators.csv format

### Votes not saving
- **Election status**: Ensure election is ACTIVE
- **CSV permissions**: Check write permissions for CSV files
- **File location**: Ensure CSV files are in project root

### Import fails
- **CSV format**: Ensure header row is `voterId,password`
- **File encoding**: Use UTF-8 encoding
- **File path**: Check file path is correct

### Buttons not visible
- **Window size**: Resize window if needed
- **Scroll**: Check if content is scrolled out of view
- **Restart**: Restart application

## 📝 Requirements Coverage

### Functional Requirements
- ✅ **FR-A07**: Bulk voter import from CSV
- ✅ **FR-C01**: Single vote enforcement (no duplicate voting)

### Non-Functional Requirements
- ✅ **NFR-S01**: SHA-256 password hashing

## 👥 User Roles

### Administrator
- Full system access
- Manage candidates and voters
- Control election status
- View results

### Voter
- Login with credentials
- Cast single vote
- View voting status

## 🔄 Workflow

1. **Administrator Setup**:
   - Login as admin
   - Add candidates
   - Register voters (manual or bulk import)
   - Start election

2. **Voter Voting**:
   - Voter logs in
   - Selects candidate
   - Casts vote
   - System prevents duplicate voting

3. **Results**:
   - Administrator views results
   - Results sorted by vote count
   - Election can be stopped

## 📚 Code Documentation

All classes and public methods include comprehensive Javadoc comments. Generate documentation using:

```bash
javadoc -d docs -sourcepath src/main/java -subpackages model controller view utils
```

## 🎯 Future Enhancements

Potential improvements:
- Database integration (replace CSV)
- Real-time result updates
- Email notifications
- Vote audit trail
- Multi-language support
- Advanced reporting

## 📄 License

This project is developed for educational purposes as a university final project.

## 👨‍💻 Author

Online Voting System - University Final Project

---

**Version**: 1.0  
**Last Updated**: 2024

