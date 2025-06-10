# 🌍 Disaster Management and Rapid Alert System

An intelligent desktop application built using **JavaFX** and **MySQL**, designed to provide real-time disaster alerts, manage affected zones, and support emergency response through an intuitive dashboard and mapping tools.

---

## 📌 Features

- 🔴 **Real-Time Alert Dashboard**
  - Displays alerts by severity, location, and type
  - Severity levels visually highlighted (Low → Critical)

- 🧭 **Interactive Map Integration (Leaflet via WebView)**
  - Shows disaster locations on a global map
  - Uses latitude and longitude data for geolocation

- 🗂️ **Pagination and Search**
  - View alerts in a paginated format
  - Dynamic search bar for filtering by type or location

- ✍️ **Alert Creation Dialog**
  - Add new alerts via a custom form
  - Automatically validates inputs and updates database

- 📊 **Live Statistics Panel**
  - Shows counts of active alerts per severity
  - Displays last update timestamp

- ✅ **User Management**
  - Associate alerts with registered users
  - Supports foreign key references and soft deletion

---

## 🧰 Tech Stack

| Component        | Technology               |
|------------------|--------------------------|
| Language         | Java                     |
| UI Framework     | JavaFX (FXML + CSS)      |
| DBMS             | MySQL                    |
| Map Integration  | Leaflet.js (via WebView) |
| Build Tool       | Maven                    |
| Architecture     | MVC (Model-View-Controller) |

---

## 🛠️ Project Structure

```

src/
├── controller/        # JavaFX controllers
├── model/             # POJO classes for Alert, User, etc.
├── dao/               # Data Access Objects (JDBC)
├── utils/             # DBConnector and helpers
├── view/              # FXML files and styles
├── resources/         # Icons, images, and styles.css
└── Main.java          # Application entry point

````

---

## 🗄️ Database Schema

Includes tables:
- `users`
- `alerts`
- `zones`
- `evacuation_routes`
- `user_alerts` (many-to-many)

Run the [`schema.sql`](./schema.sql) file to initialize the database with sample data covering multiple disaster types across continents.

---

## 🔧 Setup Instructions

1. **Clone the repo**  
   ```bash
   git clone https://github.com/yourusername/disaster-alert-system.git
   cd disaster-alert-system
````

2. **Import into IntelliJ or VS Code** (ensure JavaFX + Maven support is enabled)

3. **Set up MySQL database**

   * Run the provided SQL schema to create tables and seed data:

     ```bash
     mysql -u root -p < schema.sql
     ```

4. **Configure database in `DBConnector.java`**

   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/disaster_db";
   private static final String USER = "root";
   private static final String PASS = "yourpassword";
   ```

5. **Run the app**

   * From `Main.java` or using Maven:

     ```bash
     mvn javafx:run
     ```

---

## 📷 Screenshots

> Insert screenshots of:
>
> * Alert dashboard
> * Add Alert Dialog
> * Interactive Map
> * Severity Highlights

---

## 📄 Documentation

Full A4-format documentation submitted which contains :-

* Architecture
* UI design
* Code quality
* Database design
* Future improvements

---

## 🌐 Credits

* Map Rendering: [Leaflet.js](https://leafletjs.com)
* JavaFX Icons: [FontAwesomeFX](https://bitbucket.org/Jerady/fontawesomefx/)
* Database: MySQL Community Edition
* JavaFX Build Support: [OpenJFX](https://openjfx.io/)

---

## 📚 License

This project is for academic demonstration purposes. You are free to modify it for personal or educational use.

---

## 🤝 Contributors

* Vihaan Mathur and Sonali Singh – Developer & Designer
* Special thanks to mentors and peers for feedback during evaluation phases

