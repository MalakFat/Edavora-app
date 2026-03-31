# Edavora - Educational Management System 🎓

Edavora is a comprehensive desktop application developed as a Database course project. It facilitates the management of educational processes between students, teachers, and administrators through a sleek JavaFX GUI and a robust relational database.

## 🚀 Features

The system manages three distinct user roles with specific functionalities:

### 1. Manager (Administrator)
* **User Management:** Full CRUD operations for Students and Teachers.
* **Course Administration:** Create, update, and manage course details.
* **Communication:** Send system-wide messages and handle communications.

### 2. Teacher
* **Course Management:** Manage assigned courses and upload educational materials.
* **Content Delivery:** Add "Slides" (URLs) and "External Links" for students.
* **Attendance Tracking:** Create lectures and record student attendance (Present/Absent).
* **Salary View:** Access profile and salary information.

### 3. Student
* **Enrollment:** Browse and enroll in available courses.
* **Learning Hub:** Access course materials, slides, and shared links.
* **Attendance Tracking:** View personal attendance records for each lecture.
* **Notifications:** Receive messages from the management.

## 🛠️ Tech Stack
* **Language:** Java
* **GUI Framework:** JavaFX
* **Database:** SQL (Relational Database Design)
* **Architecture:** Object-Oriented Programming (OOP) with Entity Relationship (ER) implementation.

## 📊 Database Design (ER Highlights)
The system is built on a complex relational schema involving:
* **Inheritance:** A base `User` entity specializing into Student, Teacher, and Manager.
* **M:N Relationships:** * `Studies`: Connects Students to Courses.
    * `Attends`: Tracks Students in specific Lectures.
    * `Receives`: Manages Messages between multiple Users.
    * `Banned_From`: Handles administrative restrictions.
* **1:N Relationships:** Courses to Lectures, Slides, and Links.

## 📸 Demo
(https://drive.google.com/file/d/1ClENwRTzwo6vowpLE_gn5h5t5y2OE3_A/view?usp=drive_link)
## ⚙️ Installation & Setup
1. Clone the repository:
   ```bash
   git clone [https://github.com/MalakFat/Edavora-app.git](https://github.com/MalakFat/Edavora-app.git)
