package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import application.Banned.BannedStudent;
import application.CourseController.Course;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;


	public class ManagerController implements Initializable {

	    // Existing FXML elements (from original ManagerController)
	    @FXML private Button home;
	    @FXML private Button profile;
	    @FXML private Button message;
	    @FXML private Button logout;
	    @FXML private Button adds;
	    @FXML private Button addt;
	    @FXML private Button addc;
	    @FXML private Button student1;
	    @FXML private Button teacher1;
	    @FXML private Button course1;
	    @FXML private Text namemaneger;
	    @FXML private TextField emailF, fnameF, lnameF;
	    @FXML private PasswordField passF;
	    @FXML private DatePicker dateF;
	    @FXML private RadioButton maleF, femaleF;
	    @FXML private ImageView profileImageView;
	    @FXML private ScrollPane scrollPane;
	    @FXML private TextArea txtMessage;
	    @FXML private CheckBox chkTeacher;
	    @FXML private CheckBox chkStudent;
	    @FXML private Button addButton;
	    @FXML private Spinner<Integer> maxSpinner;
	    @FXML private ComboBox<String> FH, FM, TH, TM, teachercom;
	    @FXML private TextField courseName;
	    @FXML private TableView<StudentAbsence> tbl;
	    @FXML private TableColumn<StudentAbsence, String> clmfiestname;
	    @FXML private TableColumn<StudentAbsence, String> clmlastname;
	    @FXML private TableColumn<StudentAbsence, String> clmemail;
	    @FXML private TableColumn<StudentAbsence, LocalDate> clmbirth;
	    @FXML private TableColumn<StudentAbsence, Integer> clmage;
	    @FXML private TableColumn<StudentAbsence, String> clmgender;
	    @FXML private TableColumn<StudentAbsence, String> clmcoursename;
	    @FXML private TableColumn<StudentAbsence, Integer> clmabscount;
	    @FXML private TextField firstname;
	    @FXML private TextField lastname;
	    @FXML private TextField email;
	    @FXML private ComboBox<String> coursename;
	    @FXML private TextField age;
	    @FXML private DatePicker birth;
	    @FXML private RadioButton male;
	    @FXML private RadioButton female;
	    @FXML private ToggleGroup ll;
	    @FXML private Button btnserch;
	    @FXML private Button btnupdat;
	    @FXML private Button btndelete;
	    @FXML private TextField addFirstname;
	    @FXML private TextField addLastname;
	    @FXML private TextField addEmail;
	    @FXML private PasswordField addPassword;
	    @FXML private DatePicker addBirth;
	    @FXML private RadioButton addMale;
	    @FXML private RadioButton addFemale;
	    @FXML private ToggleGroup addTow;
	    @FXML private Button saveButton;
	    @FXML private Button backToSearchButton;
	    @FXML private Label emaillabel;
	    @FXML private TextField teacherFirstname;
	    @FXML private TextField teacherLastname;
	    @FXML private TextField teacherEmail;
	    @FXML private PasswordField teacherPasswordField;
	    @FXML private TextField teacherSalary;
	    @FXML private DatePicker teacherBirth;
	    @FXML private RadioButton teacherMale;
	    @FXML private RadioButton teacherFemale;
	    @FXML private ToggleGroup teacherTow;
	    @FXML private Button teacherSaveButton;
	    @FXML private Button teacherTogglePasswordVisibilityButton;
	    @FXML private Button teacherBackToSearchButton;
	    @FXML private Label teacherEmaillabel;
	    @FXML private TableView<Teacher> tblt;
	    @FXML private TableColumn<Teacher, String> colNamet;
	    @FXML private TableColumn<Teacher, String> collastnamet;
	    @FXML private TableColumn<Teacher, LocalDate> colBirthDatet;
	    @FXML private TableColumn<Teacher, Integer> colAget;
	    @FXML private TableColumn<Teacher, String> colGendert;
	    @FXML private TableColumn<Teacher, Double> colSalary;
	    @FXML private TableColumn<Teacher, String> colEmailt;
	    @FXML private TextField firstnamet;
	    @FXML private TextField lastnamet;
	    @FXML private TextField emailt;
	    @FXML private TextField salaryt;
	    @FXML private TextField aget;
	    @FXML private DatePicker birtht;
	    @FXML private RadioButton malet;
	    @FXML private RadioButton femalet;
	    @FXML private ToggleGroup kk;
	    @FXML private Button btnsearcht;
	    @FXML private Button btnremovet;
	    @FXML private Button btnupdatt;
	    @FXML private TableView<Course> tableView;
	    @FXML private TableColumn<Course, String> clmname;
	    @FXML private TableColumn<Course, Integer> clmmax;
	    @FXML private TableColumn<Course, String> clmteachername;
	    @FXML private TableColumn<Course, String> clmstart;
	    @FXML private TableColumn<Course, String> clmend;
	    @FXML private TableColumn<Course, Integer> clmmax1;
	    @FXML private TextField name;
	    @FXML private TextField maxcapacity;
	    @FXML private ComboBox<String> teachername;
	    @FXML private TextField courseen;
	    @FXML private ComboBox<String> FHc;
	    @FXML private ComboBox<String> FMc;
	    @FXML private ComboBox<String> THc;
	    @FXML private ComboBox<String> TMc;
	    @FXML private Button btnsearch;
	    @FXML private Button btnremove;
	    @FXML private Button btnupdatc;
	    @FXML private PasswordField currentPasswordField;
	    @FXML private TextField currentPasswordTextField;
	    @FXML private Button togglePasswordVisibilityButton;
	    @FXML private PasswordField newPasswordField;
	    @FXML private TextField newPasswordTextField;
	    @FXML private Button togglePasswordVisibilityButton2;
	    @FXML private PasswordField repeatPasswordField;
	    @FXML private TextField repeatPasswordTextField;
	    @FXML private Button togglePasswordVisibilityButton21;
	    @FXML private Label errorLabel;
	    @FXML private Button homeBtn;
	    @FXML private Button addCoursesBtn;
	    @FXML private Button profileBtn;
	    @FXML private Button messagesBtn;
	    @FXML private Button logoutBtn;

	    // New FXML elements from Banned class (renamed to avoid conflicts)
	    @FXML private TableView<BannedStudent> bannedTableView;
	    @FXML private TableColumn<BannedStudent, String> clmBannedName;
	    @FXML private TableColumn<BannedStudent, String> clmBannedCourse;
	    @FXML private Button btnUnban;

	    private byte[] profileImageBytes = null;
	    private byte[] selectedImageBytes;
	    private Connection conn;
	    private FlowPane flowPane;
	    private ObservableList<StudentAbsence> data = FXCollections.observableArrayList();
	    private ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
	    private ObservableList<Course> courseList = FXCollections.observableArrayList();
	    private ObservableList<BannedStudent> bannedList = FXCollections.observableArrayList();
	    private boolean searchPerformed = false;
	    private Map<String, String> teacherMap = new HashMap<>();

    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "malak";
    private final String PASSWORD = "123456";

    public static class StudentAbsence {
        private String firstname;
        private String lastname;
        private String email;
        private LocalDate birthDate;
        private int age;
        private String gender;
        private String courseName;
        private int absenceCount;

        public StudentAbsence(String firstname, String lastname, String email, LocalDate birthDate, int age, String gender, String courseName, int absenceCount) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.birthDate = birthDate;
            this.age = age;
            this.gender = gender;
            this.courseName = courseName;
            this.absenceCount = absenceCount;
        }

        public String getFirstname() { return firstname; }
        public String getLastname() { return lastname; }
        public String getEmail() { return email; }
        public LocalDate getBirthDate() { return birthDate; }
        public int getAge() { return age; }
        public String getGender() { return gender; }
        public String getCourseName() { return courseName; }
        public int getAbsenceCount() { return absenceCount; }
    }

    public static class Teacher {
        private final String firstname;
        private final String lastname;
        private final String email;
        private final String gender;
        private final LocalDate birthdate;
        private final Integer age;
        private final Double salary;

        public Teacher(String firstname, String lastname, String email, String gender, LocalDate birthdate, Integer age, Double salary) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.gender = gender;
            this.birthdate = birthdate;
            this.age = age;
            this.salary = salary;
        }

        public String getFirstname() { return firstname; }
        public String getLastname() { return lastname; }
        public String getEmail() { return email; }
        public String getGender() { return gender; }
        public LocalDate getBirthdate() { return birthdate; }
        public Integer getAge() { return age; }
        public Double getSalary() { return salary; }
    }
    
    
        
 // Methods from Banned class
    
    
    private void loadBannedStudents() {
        bannedList.clear();
        String query = "SELECT b.student_email, c.name_course, b.course_id " +
                      "FROM banned b " +
                      "JOIN course c ON b.course_id = c.course_id";
        try (Connection conn = connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                bannedList.add(new BannedStudent(
                    rs.getString("student_email"),
                    rs.getString("name_course"),
                    rs.getInt("course_id")
                ));
            }
            bannedTableView.setItems(bannedList);
        } catch (SQLException e) {
            showErrorAlert("Failed to load banned students", e.getMessage());
        }
    }

    private void unbannedSelectedStudent() {
        BannedStudent selected = bannedTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String query = "DELETE FROM banned WHERE student_email = ? AND course_id = ?";
            try (Connection conn = connectDB();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, selected.getStudentEmail());
                pstmt.setInt(2, selected.getCourseId());
                pstmt.executeUpdate();
                loadBannedStudents();
                new Alert(Alert.AlertType.INFORMATION, "Student unbanned successfully.").showAndWait();
            } catch (SQLException e) {
                showErrorAlert("Failed to unban student", e.getMessage());
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a student to unban.").showAndWait();
        }
    }
 
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	// New initialization for banned students (from Banned class)
        if (bannedTableView != null) {
            clmBannedName.setCellValueFactory(new PropertyValueFactory<>("studentEmail"));
            clmBannedCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
            loadBannedStudents();
            btnUnban.setOnAction(e -> unbannedSelectedStudent());
        }
        
    	// Initialize course management components
    	
    	if (emailF != null) {
    	loadUserData();
    	 fnameF.setEditable(true);
         lnameF.setEditable(true);
    	}
        if (maxSpinner != null) {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, 40);
            maxSpinner.setValueFactory(valueFactory);
        }

        if (FH != null && FM != null && TH != null && TM != null) {
            ObservableList<String> hours = FXCollections.observableArrayList();
            ObservableList<String> minutes = FXCollections.observableArrayList();
            for (int i = 0; i < 24; i++) {
                hours.add(String.format("%02d", i));
            }
            for (int i = 0; i < 60; i += 5) {
                minutes.add(String.format("%02d", i));
            }
            FH.setItems(hours);
            FM.setItems(minutes);
            TH.setItems(hours);
            TM.setItems(minutes);
            FH.setVisibleRowCount(5);
            FM.setVisibleRowCount(5);
            TH.setVisibleRowCount(5);
            TM.setVisibleRowCount(5);
        }

        initializeDatabaseConnection();
        if (teachercom != null) {
            populateTeachers();
        }
        if (emailF != null) {
            loadUserData();
        }

        // Initialize student search components (UStudentSearch.fxml)
        if (tbl != null) {
            clmfiestname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            clmlastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            clmemail.setCellValueFactory(new PropertyValueFactory<>("email"));
            clmbirth.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
            clmage.setCellValueFactory(new PropertyValueFactory<>("age"));
            clmgender.setCellValueFactory(new PropertyValueFactory<>("gender"));
            clmcoursename.setCellValueFactory(new PropertyValueFactory<>("courseName"));
            clmabscount.setCellValueFactory(new PropertyValueFactory<>("absenceCount"));

            loadCoursesToComboBox1();
            loadData1("");

            tbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    firstname.setText(newSelection.getFirstname());
                    lastname.setText(newSelection.getLastname());
                    email.setText(newSelection.getEmail());
                    birth.setValue(newSelection.getBirthDate());
                    age.setText(String.valueOf(newSelection.getAge()));
                    coursename.getSelectionModel().select(newSelection.getCourseName() != null && !newSelection.getCourseName().equals("None") ? newSelection.getCourseName() : "All Courses");
                    if ("Male".equals(newSelection.getGender())) {
                        male.setSelected(true);
                        female.setSelected(false);
                    } else if ("Female".equals(newSelection.getGender())) {
                        female.setSelected(true);
                        male.setSelected(false);
                    } else {
                        male.setSelected(false);
                        female.setSelected(false);
                    }
                } else {
                    clearSearchFields();
                }
            });
            
            
        }
       
        if (tableView != null) {
            clmname.setCellValueFactory(new PropertyValueFactory<>("nameCourse"));
            clmmax.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
            clmteachername.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
            clmstart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            clmend.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            clmmax1.setCellValueFactory(new PropertyValueFactory<>("enrolledStudents"));

            tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    name.setText(newSelection.getNameCourse());
                    maxcapacity.setText(String.valueOf(newSelection.getMaxCapacity()));
                    teachername.setValue(newSelection.getTeacherName());
                    courseen.setText(String.valueOf(newSelection.getEnrolledStudents()));
                    String[] startTimeParts = newSelection.getStartTime().split(":");
                    if (startTimeParts.length >= 2) {
                        FHc.setValue(startTimeParts[0]);
                        FMc.setValue(startTimeParts[1]);
                    }
                    String[] endTimeParts = newSelection.getEndTime().split(":");
                    if (endTimeParts.length >= 2) {
                        THc.setValue(endTimeParts[0]);
                        TMc.setValue(endTimeParts[1]);
                    }
                } else {
                    clearCourseFields();
                }
            });

            // Initialize time ComboBoxes
            ObservableList<String> hours = FXCollections.observableArrayList();
            for (int i = 0; i < 24; i++) {
                hours.add(String.format("%02d", i));
            }
            ObservableList<String> minutes = FXCollections.observableArrayList();
            for (int i = 0; i < 60; i += 5) {
                minutes.add(String.format("%02d", i));
            }
            FHc.setItems(hours);
            FMc.setItems(minutes);
            THc.setItems(hours);
            TMc.setItems(minutes);
            FHc.setVisibleRowCount(5);
            FMc.setVisibleRowCount(5);
            THc.setVisibleRowCount(5);
            TMc.setVisibleRowCount(5);

            // Bind buttons to actions
            btnsearch.setOnAction(e -> searchCourse());
            btnremove.setOnAction(e -> removeCourse());
            btnupdatc.setOnAction(e -> updateCourse());

            loadTeachersForCourses();
            loadCoursesFromDB();
        }

        initializeDatabaseConnection();
        if (teachercom != null) {
            populateTeachers();
        }
        if (emailF != null) {
            loadUserData();
        }
        
     
       
        // Initialize teacher search components (UTeacherSearch.fxml)
        if (tblt != null) {
            colNamet.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            collastnamet.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            colEmailt.setCellValueFactory(new PropertyValueFactory<>("email"));
            colBirthDatet.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
            colAget.setCellValueFactory(new PropertyValueFactory<>("age"));
            colGendert.setCellValueFactory(new PropertyValueFactory<>("gender"));
            colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

            loadTeachersFromDB();

            tblt.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    firstnamet.setText(newSelection.getFirstname());
                    lastnamet.setText(newSelection.getLastname());
                    emailt.setText(newSelection.getEmail());
                    salaryt.setText(String.valueOf(newSelection.getSalary()));
                    birtht.setValue(newSelection.getBirthdate());
                    aget.setText(String.valueOf(newSelection.getAge()));
                    if ("Male".equals(newSelection.getGender())) {
                        malet.setSelected(true);
                        femalet.setSelected(false);
                    } else if ("Female".equals(newSelection.getGender())) {
                        femalet.setSelected(true);
                        malet.setSelected(false);
                    } else {
                        malet.setSelected(false);
                        femalet.setSelected(false);
                    }
                } else {
                    clearTeacherFields();
                }
            });

            // Synchronize birthdate and age
            if (birtht != null) {
                birtht.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        int newAge = Period.between(newVal, LocalDate.now()).getYears();
                        aget.setText(String.valueOf(newAge));
                    }
                });
            }

            if (aget != null) {
                aget.textProperty().addListener((obs, oldText, newText) -> {
                    if (!newText.isEmpty()) {
                        try {
                            int newAge = Integer.parseInt(newText);
                            if (newAge > 0) {
                                LocalDate newBirth = LocalDate.now().minusYears(newAge);
                                birtht.setValue(newBirth);
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid input
                        }
                    }
                });
            }
        }

        // Initialize add student components (UAddStudent.fxml)
        if (emaillabel != null) {
            emaillabel.setVisible(false);
        }
        if (saveButton != null) {
            saveButton.setOnAction(e -> saveStudent());
        }
        if (backToSearchButton != null) {
            backToSearchButton.setOnAction(e -> handleShowStudents(new ActionEvent(backToSearchButton, null)));
        }

        // Initialize add teacher components (UAddTeacher.fxml)
        if (teacherEmaillabel != null) {
            teacherEmaillabel.setVisible(false);
        }
        if (teacherSaveButton != null) {
            teacherSaveButton.setOnAction(e -> saveTeacher());
        }
     
        // Bind button actions for student search
        if (btnserch != null) {
            btnserch.setOnAction(e -> handleSearchStudents());
        }
        if (btnupdat != null) {
            btnupdat.setOnAction(e -> handleUpdateStudent());
        }
        if (btndelete != null) {
            btndelete.setOnAction(e -> handleDeleteStudent());
        }

        // Bind button actions for teacher search
        if (btnsearcht != null) {
            btnsearcht.setOnAction(e -> searchTeacher());
        }
        if (btnupdatt != null) {
            btnupdatt.setOnAction(e -> updateTeacher());
        }
        if (btnremovet != null) {
            btnremovet.setOnAction(e -> removeTeacher());
        }
        
    }

    
        
    private Connection connectDB() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void initializeDatabaseConnection() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to connect to database: " + e.getMessage()).showAndWait();
        }
    }
    
    // Course management methods
    private void loadTeachersForCourses() {
        ObservableList<String> teachers = FXCollections.observableArrayList();
        teacherMap.clear();
        try (Connection conn = connectDB()) {
            String query = """
                    SELECT u.firstname || ' ' || u.lastname AS teacher_name, u.email
                    FROM public.teacher t
                    JOIN public."User" u ON t.email = u.email
                    ORDER BY u.firstname, u.lastname
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String teacherName = rs.getString("teacher_name");
                    String email = rs.getString("email");
                    teachers.add(teacherName);
                    teacherMap.put(teacherName, email);
                }
                teachername.setItems(teachers);
                teachername.setVisibleRowCount(5);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading teachers: " + e.getMessage()).showAndWait();
        }
    }

  
    
    private void loadCoursesFromDB() {
        courseList.clear();
        try (Connection conn = connectDB()) {
            String query = """
                    SELECT c.course_id, c.name_course, c.start_time, c.end_time, c.max_capacity,
                           u.firstname || ' ' || u.lastname AS teacher_name,
                           c.email_teacher AS teacher_email,
                           COUNT(s.student_email) AS enrolled_students
                    FROM public.course c
                    JOIN public.teacher t ON c.email_teacher = t.email
                    JOIN public."User" u ON t.email = u.email
                    LEFT JOIN public.study s ON c.course_id = s.course_id
                    GROUP BY c.course_id, c.name_course, c.start_time, c.end_time, c.max_capacity, u.firstname, u.lastname, c.email_teacher
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String startTime = rs.getTime("start_time").toString().substring(0, 5);
                    String endTime = rs.getTime("end_time").toString().substring(0, 5);
                    courseList.add(new Course(
                            rs.getInt("course_id"),
                            rs.getString("name_course"),
                            startTime,
                            endTime,
                            rs.getInt("max_capacity"),
                            rs.getString("teacher_name"),
                            rs.getString("teacher_email"),
                            rs.getInt("enrolled_students")
                    ));
                }
                tableView.setItems(courseList);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading courses: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void searchCourse() {
        String nameText = name.getText().trim();
        String maxCapacityText = maxcapacity.getText().trim();
        String teacherNameText = teachername.getValue() != null ? teachername.getValue().trim() : "";
        String courseEnText = courseen.getText().trim();
        String startTimeText = FHc.getValue() != null && FMc.getValue() != null 
                ? FHc.getValue() + ":" + FMc.getValue() + ":00" : "";
        String endTimeText = THc.getValue() != null && TMc.getValue() != null 
                ? THc.getValue() + ":" + TMc.getValue() + ":00" : "";

        if (nameText.isEmpty() && maxCapacityText.isEmpty() && teacherNameText.isEmpty() && courseEnText.isEmpty() && startTimeText.isEmpty() && endTimeText.isEmpty()) {
            loadCoursesFromDB();
            searchPerformed = false;
            return;
        }

        StringBuilder query = new StringBuilder("""
                SELECT c.course_id, c.name_course, c.start_time, c.end_time, c.max_capacity,
                       u.firstname || ' ' || u.lastname AS teacher_name,
                       c.email_teacher AS teacher_email,
                       COUNT(s.student_email) AS enrolled_students
                FROM public.course c
                JOIN public.teacher t ON c.email_teacher = t.email
                JOIN public."User" u ON t.email = u.email
                LEFT JOIN public.study s ON c.course_id = s.course_id
                WHERE 1=1
                """);
        ArrayList<Object> params = new ArrayList<>();

        if (!nameText.isEmpty()) {
            query.append(" AND LOWER(c.name_course) LIKE LOWER(?)");
            params.add("%" + nameText + "%");
        }
        if (!maxCapacityText.isEmpty()) {
            try {
                int maxCapacityValue = Integer.parseInt(maxCapacityText);
                query.append(" AND c.max_capacity = ?");
                params.add(maxCapacityValue);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Please enter a valid max capacity.", ButtonType.OK).showAndWait();
                return;
            }
        }
        if (!teacherNameText.isEmpty()) {
            query.append(" AND LOWER(u.firstname || ' ' || u.lastname) LIKE LOWER(?)");
            params.add("%" + teacherNameText + "%");
        }
        if (!startTimeText.isEmpty()) {
            query.append(" AND c.start_time = ?");
            params.add(Time.valueOf(startTimeText));
        }
        if (!endTimeText.isEmpty()) {
            query.append(" AND c.end_time = ?");
            params.add(Time.valueOf(endTimeText));
        }

        query.append(" GROUP BY c.course_id, c.name_course, c.start_time, c.end_time, c.max_capacity, u.firstname, u.lastname, c.email_teacher");

        if (!courseEnText.isEmpty()) {
            try {
                int enrolledStudents = Integer.parseInt(courseEnText);
                query.append(" HAVING COUNT(s.student_email) = ?");
                params.add(enrolledStudents);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Please enter a valid number of enrolled students.", ButtonType.OK).showAndWait();
                return;
            }
        }

        try (Connection conn = connectDB()) {
            PreparedStatement pstmt = conn.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    pstmt.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) param);
                } else if (param instanceof Time) {
                    pstmt.setTime(i + 1, (Time) param);
                }
            }

            ResultSet rs = pstmt.executeQuery();
            courseList.clear();
            while (rs.next()) {
                String startTime = rs.getTime("start_time").toString().substring(0, 5);
                String endTime = rs.getTime("end_time").toString().substring(0, 5);
                courseList.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("name_course"),
                        startTime,
                        endTime,
                        rs.getInt("max_capacity"),
                        rs.getString("teacher_name"),
                        rs.getString("teacher_email"),
                        rs.getInt("enrolled_students")
                ));
            }
            tableView.setItems(courseList);
            if (courseList.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No matching courses found.", ButtonType.OK).showAndWait();
            }
            searchPerformed = true;
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error searching courses: " + e.getMessage()).showAndWait();
        }
        clearCourseFields();
    }

    @FXML
    private void updateCourse() {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a course to update.", ButtonType.OK).showAndWait();
            return;
        }

        String nameText = name.getText().trim();
        String maxCapacityText = maxcapacity.getText().trim();
        String teacherNameText = teachername.getValue() != null ? teachername.getValue().trim() : "";
        String courseEnText = courseen.getText().trim();
        String startTimeText = FHc.getValue() != null && FMc.getValue() != null 
                ? FHc.getValue() + ":" + FMc.getValue() + ":00" : "";
        String endTimeText = THc.getValue() != null && TMc.getValue() != null 
                ? THc.getValue() + ":" + TMc.getValue() + ":00" : "";

        if (nameText.isEmpty() && maxCapacityText.isEmpty() && teacherNameText.isEmpty() && courseEnText.isEmpty() && startTimeText.isEmpty() && endTimeText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter at least one value to update.", ButtonType.OK).showAndWait();
            return;
        }

        // Validate enrolled students
        if (!courseEnText.isEmpty()) {
            try {
                int inputEnrolled = Integer.parseInt(courseEnText);
                if (inputEnrolled != selectedCourse.getEnrolledStudents()) {
                    new Alert(Alert.AlertType.WARNING, "You cannot change the number of enrolled students. It must match the calculated value.", ButtonType.OK).showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Please enter a valid number for enrolled students.", ButtonType.OK).showAndWait();
                return;
            }
        }

        // Determine new values for time and teacher
        Time newStartTime = !startTimeText.isEmpty() ? Time.valueOf(startTimeText) : Time.valueOf(selectedCourse.getStartTime() + ":00");
        Time newEndTime = !endTimeText.isEmpty() ? Time.valueOf(endTimeText) : Time.valueOf(selectedCourse.getEndTime() + ":00");
        String newTeacherEmail = !teacherNameText.isEmpty() && !teacherNameText.equals(selectedCourse.getTeacherName()) 
                ? teacherMap.get(teacherNameText) 
                : selectedCourse.getTeacherEmail();

        if (newTeacherEmail == null && !teacherNameText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Selected teacher not found.", ButtonType.OK).showAndWait();
            return;
        }

        // Validate start_time < end_time
        if (newStartTime.compareTo(newEndTime) >= 0) {
            new Alert(Alert.AlertType.WARNING, "Start time must be before end time.", ButtonType.OK).showAndWait();
            return;
        }

        // Check for time conflicts
        if ((!startTimeText.isEmpty() || !endTimeText.isEmpty() || !teacherNameText.isEmpty()) && hasTimeConflict(selectedCourse.getCourseId(), newTeacherEmail, newStartTime, newEndTime)) {
            new Alert(Alert.AlertType.WARNING, "The new time conflicts with another course for this teacher.", ButtonType.OK).showAndWait();
            return;
        }

        try (Connection conn = connectDB()) {
            StringBuilder updateQuery = new StringBuilder("UPDATE public.course SET ");
            ArrayList<Object> params = new ArrayList<>();
            boolean updated = false;

            if (!nameText.isEmpty()) {
                updateQuery.append("name_course = ?, ");
                params.add(nameText);
                updated = true;
            }
            if (!maxCapacityText.isEmpty()) {
                try {
                    int maxCapacityValue = Integer.parseInt(maxCapacityText);
                    if (maxCapacityValue < selectedCourse.getEnrolledStudents()) {
                        new Alert(Alert.AlertType.WARNING, "Max capacity cannot be less than the number of enrolled students (" + selectedCourse.getEnrolledStudents() + ").", ButtonType.OK).showAndWait();
                        return;
                    }
                    updateQuery.append("max_capacity = ?, ");
                    params.add(maxCapacityValue);
                    updated = true;
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.WARNING, "Please enter a valid max capacity.", ButtonType.OK).showAndWait();
                    return;
                }
            }
            if (!startTimeText.isEmpty()) {
                updateQuery.append("start_time = ?, ");
                params.add(newStartTime);
                updated = true;
            }
            if (!endTimeText.isEmpty()) {
                updateQuery.append("end_time = ?, ");
                params.add(newEndTime);
                updated = true;
            }
            if (!teacherNameText.isEmpty() && !teacherNameText.equals(selectedCourse.getTeacherName())) {
                updateQuery.append("email_teacher = ?, ");
                params.add(newTeacherEmail);
                updated = true;
            }

            if (!updated) {
                return;
            }

            updateQuery.setLength(updateQuery.length() - 2); // Remove trailing comma
            updateQuery.append(" WHERE course_id = ?");
            params.add(selectedCourse.getCourseId());

            PreparedStatement pstmt = conn.prepareStatement(updateQuery.toString());
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    pstmt.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) param);
                } else if (param instanceof Time) {
                    pstmt.setTime(i + 1, (Time) param);
                }
            }

            conn.setAutoCommit(false);
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();

            if (rowsAffected > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Course updated successfully.", ButtonType.OK).showAndWait();
                loadCoursesFromDB();
                clearCourseFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update course.", ButtonType.OK).showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error updating course: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void removeCourse() {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a course to delete.", ButtonType.OK).showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the course '" + selectedCourse.getNameCourse() + "'?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection conn = connectDB()) {
             
                    String deleteCourseSql = "DELETE FROM public.course WHERE course_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(deleteCourseSql)) {
                        pstmt.setInt(1, selectedCourse.getCourseId());
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            new Alert(Alert.AlertType.INFORMATION, "Course deleted successfully.", ButtonType.OK).showAndWait();
                            loadCoursesFromDB();
                            clearCourseFields();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Failed to delete course.", ButtonType.OK).showAndWait();
                        }
                    }
                } catch (SQLException ex) {
                    new Alert(Alert.AlertType.ERROR, "Error deleting course: " + ex.getMessage()).showAndWait();
                }
            }
        });
    }

    private boolean hasTimeConflict(int courseId, String teacherEmail, Time startTime, Time endTime) {
        try (Connection conn = connectDB()) {
            String query = """
                    SELECT start_time, end_time
                    FROM public.course
                    WHERE email_teacher = ? AND course_id != ?
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, teacherEmail);
                stmt.setInt(2, courseId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Time existingStart = rs.getTime("start_time");
                    Time existingEnd = rs.getTime("end_time");
                    if (!(endTime.toLocalTime().isBefore(existingStart.toLocalTime()) || startTime.toLocalTime().isAfter(existingEnd.toLocalTime()))) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error checking time conflict: " + e.getMessage()).showAndWait();
        }
        return false;
    }

    private void clearCourseFields() {
        name.clear();
        maxcapacity.clear();
        teachername.setValue(null);
        courseen.clear();
        FHc.setValue(null);
        FMc.setValue(null);
        THc.setValue(null);
        TMc.setValue(null);
    }

    // Student management methods
    private void loadCoursesToComboBox1() {
        ObservableList<String> courseNames = FXCollections.observableArrayList();
        courseNames.add("All Courses");
        try (Connection conn = connectDB()) {
            String query = "SELECT name_course FROM public.course ORDER BY name_course";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                courseNames.add(rs.getString("name_course"));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred while loading courses: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
        coursename.setItems(courseNames);
        coursename.getSelectionModel().selectFirst();
    }

    private void loadData1(String whereClause) {
        data.clear();
        String sql = "SELECT " +
                "u.firstname, u.lastname, " +
                "u.email, u.birthdate, u.age, u.gender, " +
                "c.name_course, COALESCE(a.absence_count, 0) AS absence_count " +
                "FROM public.\"User\" u " +
                "JOIN public.student s ON u.email = s.email " +
                "LEFT JOIN public.study st ON s.email = st.student_email " +
                "LEFT JOIN public.course c ON st.course_id = c.course_id " +
                "LEFT JOIN public.absences a " +
                "       ON s.email = a.student_email AND c.course_id = a.course_id " +
                "WHERE 1=1 " + whereClause + " " +
                "ORDER BY u.firstname, u.lastname";

        try (Connection conn = connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String email = rs.getString("email");
                LocalDate birthDate = rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null;
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String courseName = rs.getString("name_course") != null ? rs.getString("name_course") : "None";
                int absences = rs.getInt("absence_count");
                data.add(new StudentAbsence(firstname, lastname, email, birthDate, age, gender, courseName, absences));
            }
            tbl.setItems(data);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred while loading student data: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

   

    private void clearSearchFields1() {
        firstname.clear();
        lastname.clear();
        email.clear();
        birth.setValue(null);
        age.clear();
        coursename.getSelectionModel().selectFirst();
        male.setSelected(false);
        female.setSelected(false);
    }


    private void loadUserData() {
        String email = UserSession.getInstance().getEmail();
        try (Connection conn = connectDB();
             PreparedStatement stet = conn.prepareStatement(
                     "SELECT * FROM public.\"User\" WHERE email = ?")) {
            stet.setString(1, email);
            ResultSet rs = stet.executeQuery();
            if (rs.next()) {
                emailF.setText(rs.getString("email"));
                fnameF.setText(rs.getString("firstname"));
                lnameF.setText(rs.getString("lastname"));
                passF.setText(rs.getString("password"));
                Date birthdate = rs.getDate("birthdate");
                if (birthdate != null) {
                    dateF.setValue(birthdate.toLocalDate());
                }
                String gen = rs.getString("gender");
                if (gen != null) {
                    if (gen.equalsIgnoreCase("Male")) {
                        maleF.setSelected(true);
                    } else if (gen.equalsIgnoreCase("Female")) {
                        femaleF.setSelected(true);
                    }
                }
                profileImageBytes = rs.getBytes("profile_image");
                if (profileImageBytes != null && profileImageBytes.length > 0) {
                    profileImageView.setImage(new Image(new ByteArrayInputStream(profileImageBytes)));
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "User not found", ButtonType.OK).showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }
    @FXML
    private void handleChangePassword() {
        try {
            Stage stage = (Stage) emailF.getScene().getWindow();
            stage.close();

            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("UManagerChangePassword.fxml"));
            newStage.setScene(new Scene(root));
            newStage.setTitle("Change Password");
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangeProfilePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                profileImageBytes = readFileToBytes(selectedFile);
                profileImageView.setImage(new Image(new ByteArrayInputStream(profileImageBytes)));
                String email = UserSession.getInstance().getEmail();
                String sql = "UPDATE public.\"User\" SET profile_image=? WHERE email=?";
                try (Connection conn = connectDB();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setBytes(1, profileImageBytes);
                    pstmt.setString(2, email);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage(), ButtonType.OK).showAndWait();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSave() {
        String email = UserSession.getInstance().getEmail();
        String sql = "UPDATE public.\"User\" SET firstname=?, lastname=?, birthdate=?, gender=?, profile_image=? WHERE email=?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fnameF.getText());
            pstmt.setString(2, lnameF.getText());
            LocalDate date = dateF.getValue();
            if (date != null) {
                pstmt.setDate(3, Date.valueOf(date));
            } else {
                pstmt.setNull(3, Types.DATE);
            }
            String gender = maleF.isSelected() ? "Male" : "Female";
            pstmt.setString(4, gender);
            if (profileImageBytes != null) {
                pstmt.setBytes(5, profileImageBytes);
            } else {
                pstmt.setNull(5, Types.BINARY);
            }
            pstmt.setString(6, email);
            pstmt.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Profile Updated");
            alert.setHeaderText(null);
            alert.setContentText("Your profile information has been updated successfully.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }


    private byte[] readFileToBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int read;
            while ((read = fis.read(buf)) != -1) {
                bos.write(buf, 0, read);
            }
            return bos.toByteArray();
        }
    }
    @FXML
    private void openHome(ActionEvent event) {
        loadScene(event, "UManagerMain.fxml", "Home Screen");
    }

    @FXML
    private void openProfile(ActionEvent event) {
        loadScene(event, "UManagerProfile.fxml", "Manager Profile");
    }

    @FXML
    private void openMessageWindow(ActionEvent event) {
        loadScene(event, "UManagerMessage.fxml", "Message Window");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene(event, "ULogin.fxml", "Login");
    }

    @FXML
    private void handleAddStudent(ActionEvent event) {
        loadScene(event, "UAddStudent.fxml", "Add Student");
    }

    @FXML
    private void handleAddTeacher(ActionEvent event) {
        loadScene(event, "UAddTeacher.fxml", "Add Teacher");
    }

    @FXML
    private void handleAddCourse(ActionEvent event) {
        loadScene(event, "UAddCourse.fxml", "Add Course");
    }

    @FXML
    private void handleShowStudents(ActionEvent event) {
        loadScene(event, "UStudentSearch.fxml", "Student Search");
    }

    @FXML
    private void handleShowTeachers(ActionEvent event) {
        loadScene(event, "UTeacherSearch.fxml", "Teachers List");
    }

    @FXML
    private void handleShowCourses(ActionEvent event) {
        loadScene(event, "UCourseSearch.fxml", "Courses List");
    }

    @FXML
    private void handleBanned(ActionEvent event) {
        loadScene(event, "UBanned.fxml", "Banned Students");
    }
    @FXML
    private void handleShow1() {
    	try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","malak","123456");
            File file = new File("src/main/java/application/StudentReport.jrxml");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null,"This file isn't exsist","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            FileInputStream ins = new FileInputStream(file);
            JasperDesign jd = JRXmlLoader.load(ins);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, con);

            
            JFrame reportFrame = new JFrame("Student Report");
            reportFrame.getContentPane().add(new JRViewer(jp));
            reportFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            reportFrame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
    

    
    @FXML
    private void handleShow2(ActionEvent event) {
    	try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","malak","123456");
            File file = new File("src/main/java/application/TeacherReport.jrxml");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null,"This file isn't exsist","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            FileInputStream ins = new FileInputStream(file);
            JasperDesign jd = JRXmlLoader.load(ins);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, con);

            
            JFrame reportFrame = new JFrame("Teacher Report");
            reportFrame.getContentPane().add(new JRViewer(jp));
            reportFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            reportFrame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }

    @FXML
    private void handleShow3(ActionEvent event) {
    	try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","malak","123456");
            File file = new File("src/main/java/application/CourseReport.jrxml");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null,"This file isn't exsist","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            FileInputStream ins = new FileInputStream(file);
            JasperDesign jd = JRXmlLoader.load(ins);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, con);

            
            JFrame reportFrame = new JFrame("Course Report");
            reportFrame.getContentPane().add(new JRViewer(jp));
            reportFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            reportFrame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }

    private void loadScene(ActionEvent event, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Failed to load " + title, e.getMessage());
        }
    }

    private void showErrorAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText("An error occurred: " + message);
        alert.showAndWait();
    }


    
    private void loadMessages() {
        if (flowPane == null) return;
        flowPane.getChildren().clear();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT message_id, message_content, teacher, student FROM public.\"message\" ORDER BY message_id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("message_id");
                String content = rs.getString("message_content");
                boolean toTeacher = rs.getBoolean("teacher");
                boolean toStudent = rs.getBoolean("student");
                HBox box = createMessageBox(id, content, toTeacher, toStudent);
                flowPane.getChildren().add(box);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 
        
    private HBox createMessageBox(int id, String content, boolean toTeacher, boolean toStudent) {
        HBox hbox = new HBox();
        hbox.setStyle("-fx-border-color: #271048; -fx-border-width: 2;-fx-border-radius:20; -fx-background-radius:20; -fx-padding: 10; -fx-background-color: white;");
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(20);
        hbox.setPrefWidth(700);
        String toWhom = toTeacher && toStudent ? "To: Teachers & Students" : toTeacher ? "To: Teachers" : toStudent ? "To: Students" : "";
        Label lbl = new Label(content + "\n");
        lbl.setWrapText(true);
        lbl.setPrefWidth(600);
        lbl.setStyle("-fx-font-size: 20px; -fx-font-family: 'System';");
        Label lbl2 = new Label(toWhom);
        lbl2.setWrapText(true);
        lbl2.setPrefWidth(600);
        lbl2.setStyle("-fx-font-size: 13px; -fx-font-family: 'System';");
        Button deleteBtn = new Button("❌");
        deleteBtn.setStyle("-fx-text-fill: red;-fx-background-color: white; -fx-font-size: 20;-fx-border-radius:50; -fx-background-radius:50;");
        deleteBtn.setOnAction(e -> {
            deleteMessage(id);
            loadMessages();
        });
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5));
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(lbl, lbl2);
        hbox.getChildren().addAll(vbox, deleteBtn);
        return hbox;
    }

    private void deleteMessage(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM public.\"message\" WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendMessage() {
        String content = txtMessage.getText().trim();
        boolean toTeacher = chkTeacher.isSelected();
        boolean toStudent = chkStudent.isSelected();
        if (content.isEmpty()) {
            showAlert(AlertType.WARNING, "Message is empty!");
            return;
        }
        if (!toTeacher && !toStudent) {
            showAlert(AlertType.WARNING, "Please select at least one recipient!");
            return;
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO public.message (message_content, teacher, student) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, content);
            ps.setBoolean(2, toTeacher);
            ps.setBoolean(3, toStudent);
            ps.executeUpdate();
            showAlert(AlertType.INFORMATION, "Message sent successfully!");
            txtMessage.clear();
            chkTeacher.setSelected(false);
            chkStudent.setSelected(false);
            loadMessages();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error while saving the message!");
        }
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(addButton.getScene().getWindow());
        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                selectedImageBytes = fis.readAllBytes();
                Image image = new Image(selectedFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300);
                imageView.setFitHeight(189);
                imageView.setPreserveRatio(false);
                Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
                clip.setArcWidth(50);
                clip.setArcHeight(50);
                imageView.setClip(clip);
                addButton.setGraphic(imageView);
                addButton.setText("");
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error loading image: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    void addL(ActionEvent event) {
        try {
            if (courseName.getText().isBlank() || teachercom.getValue() == null ||
                    FH.getValue() == null || FM.getValue() == null ||
                    TH.getValue() == null || TM.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "Please fill all fields.").showAndWait();
                return;
            }
            LocalTime startTime = LocalTime.of(Integer.parseInt(FH.getValue()), Integer.parseInt(FM.getValue()));
            LocalTime endTime = LocalTime.of(Integer.parseInt(TH.getValue()), Integer.parseInt(TM.getValue()));
            if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
                new Alert(Alert.AlertType.ERROR, "End time must be after start time.").showAndWait();
                return;
            }
            String teacherEmail = teachercom.getValue().substring(teachercom.getValue().indexOf("(") + 1, teachercom.getValue().indexOf(")"));
            if (hasTimeConflict(teacherEmail, startTime, endTime)) {
                new Alert(Alert.AlertType.ERROR, "Teacher has another course at this time.").showAndWait();
                return;
            }
            saveCourseToDatabase(courseName.getText(), startTime, endTime, maxSpinner.getValue(), teacherEmail, selectedImageBytes);
            new Alert(Alert.AlertType.INFORMATION, "Course added successfully!").showAndWait();
            clearForm();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid time format.").showAndWait();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).showAndWait();
        }
    }

    private boolean hasTimeConflict(String teacherEmail, LocalTime startTime, LocalTime endTime) throws SQLException {
        String query = "SELECT start_time, end_time FROM public.course WHERE email_teacher = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, teacherEmail);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            LocalTime existingStart = rs.getTime("start_time").toLocalTime();
            LocalTime existingEnd = rs.getTime("end_time").toLocalTime();
            if (!(endTime.isBefore(existingStart) || startTime.isAfter(existingEnd))) {
                return true;
            }
        }
        return false;
    }

    private void saveCourseToDatabase(String courseName, LocalTime startTime, LocalTime endTime,
                                     int maxCapacity, String teacherEmail, byte[] photo) throws SQLException {
        String query = "INSERT INTO public.course (name_course, start_time, end_time, max_capacity, email_teacher, photo) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, courseName);
        stmt.setTime(2, Time.valueOf(startTime));
        stmt.setTime(3, Time.valueOf(endTime));
        stmt.setInt(4, maxCapacity);
        stmt.setString(5, teacherEmail);
        stmt.setBytes(6, photo);
        stmt.executeUpdate();
    }

    private void clearForm() {
        courseName.clear();
        teachercom.setValue(null);
        FH.setValue(null);
        FM.setValue(null);
        TH.setValue(null);
        TM.setValue(null);
        maxSpinner.getValueFactory().setValue(40);
        addButton.setGraphic(null);
        addButton.setText("Add Image");
        selectedImageBytes = null;
    }

    private void populateTeachers() {
        ObservableList<String> teachers = FXCollections.observableArrayList();
        try {
            String query = "SELECT u.firstname, u.lastname, u.email FROM public.\"User\" u JOIN public.teacher t ON u.email = t.email";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fullName = rs.getString("firstname") + " " + rs.getString("lastname") + " (" + rs.getString("email") + ")";
                teachers.add(fullName);
            }
            teachercom.setItems(teachers);
            teachercom.setVisibleRowCount(5);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading teachers: " + e.getMessage()).showAndWait();
        }
    }
     
    private void loadCoursesToComboBox() {
        ObservableList<String> courseNames = FXCollections.observableArrayList();
        courseNames.add("All Courses");
        try (Connection conn = connectDB()) {
            String query = "SELECT name_course FROM public.course ORDER BY name_course";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                courseNames.add(rs.getString("name_course"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while loading courses: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
        coursename.setItems(courseNames);
        coursename.getSelectionModel().selectFirst();
    }

    private void loadData(String whereClause) {
        data.clear();
        String sql = "SELECT " +
                "u.firstname, u.lastname, " +
                "u.email, u.birthdate, u.age, u.gender, " +
                "c.name_course, COALESCE(a.absence_count, 0) AS absence_count " +
                "FROM public.\"User\" u " +
                "JOIN public.student s ON u.email = s.email " +
                "LEFT JOIN public.study st ON s.email = st.student_email " +
                "LEFT JOIN public.course c ON st.course_id = c.course_id " +
                "LEFT JOIN (SELECT student_email, course_id, COUNT(*) AS absence_count FROM public.attendance WHERE attendance_status = 'Absent' GROUP BY student_email, course_id) a ON s.email = a.student_email AND c.course_id = a.course_id " +
                "WHERE 1=1 " + whereClause +
                " ORDER BY u.firstname, u.lastname";
        try (Connection conn = connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String email = rs.getString("email");
                LocalDate birthDate = rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null;
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String courseName = rs.getString("name_course") != null ? rs.getString("name_course") : "None";
                int absences = rs.getInt("absence_count");
                data.add(new StudentAbsence(firstname, lastname, email, birthDate, age, gender, courseName, absences));
            }
            tbl.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearSearchFields() {
        firstname.clear();
        lastname.clear();
        email.clear();
        birth.setValue(null);
        age.clear();
        coursename.getSelectionModel().selectFirst();
        male.setSelected(false);
        female.setSelected(false);
    }

    @FXML
    private void handleSearchStudents() {
        String firstNameText = firstname.getText().trim();
        String lastNameText = lastname.getText().trim();
        String course = coursename.getSelectionModel().getSelectedItem();
        if ("All Courses".equals(course)) {
            course = "";
        }
        String emailText = email.getText().trim();
        LocalDate birthDate = birth.getValue();
        String ageText = age.getText().trim();
        String gender = male.isSelected() ? "Male" : (female.isSelected() ? "Female" : "");
        if (firstNameText.isEmpty() && lastNameText.isEmpty() && course.isEmpty() && emailText.isEmpty() && birthDate == null && ageText.isEmpty() && gender.isEmpty()) {
            showAllStudents();
            return;
        }
        String where = "";
        if (!firstNameText.isEmpty()) {
            where += " AND u.firstname ILIKE '%" + firstNameText + "%'";
        }
        if (!lastNameText.isEmpty()) {
            where += " AND u.lastname ILIKE '%" + lastNameText + "%'";
        }
        if (!course.isEmpty()) {
            where += " AND c.name_course ILIKE '%" + course + "%'";
        }
        if (!emailText.isEmpty()) {
            where += " AND u.email ILIKE '%" + emailText + "%'";
        }
        if (birthDate != null) {
            where += " AND u.birthdate = '" + Date.valueOf(birthDate) + "'";
        }
        if (!ageText.isEmpty()) {
            where += " AND u.age = " + ageText;
        }
        if (!gender.isEmpty()) {
            where += " AND u.gender = '" + gender + "'";
        }
        loadData1(where);
        clearSearchFields();
    }

    @FXML
    private void handleUpdateStudent() {
        StudentAbsence selected = tbl.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a row to update", ButtonType.OK).showAndWait();
            return;
        }
        String selectedCourse = coursename.getSelectionModel().getSelectedItem();
        if (selectedCourse != null && !"All Courses".equals(selectedCourse) && !selectedCourse.equals(selected.getCourseName())) {
            new Alert(Alert.AlertType.WARNING, "You cannot change the course", ButtonType.OK).showAndWait();
            return;
        }
        String firstNameText = firstname.getText().trim();
        String lastNameText = lastname.getText().trim();
        String emailText = email.getText().trim();
        LocalDate birthDate = birth.getValue();
        String ageText = age.getText().trim();
        Integer newAge = null;
        if (!ageText.isEmpty()) {
            try {
                newAge = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Age must be a valid integer", ButtonType.OK).showAndWait();
                return;
            }
        }
        String gender = male.isSelected() ? "Male" : (female.isSelected() ? "Female" : "");
        if (firstNameText.isEmpty() && lastNameText.isEmpty() && emailText.isEmpty() && birthDate == null && newAge == null && gender.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill at least one field to update", ButtonType.OK).showAndWait();
            return;
        }
        if (!emailText.isEmpty() && !emailText.equals(selected.getEmail())) {
            try (Connection conn = connectDB()) {
                String checkSql = "SELECT COUNT(*) FROM public.\"User\" WHERE email = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, emailText);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        new Alert(Alert.AlertType.ERROR, "This email already exists", ButtonType.OK).showAndWait();
                        return;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An error occurred while checking the email", ButtonType.OK).showAndWait();
                return;
            }
        }
        if (birthDate != null && newAge != null) {
            int calculatedAge = Period.between(birthDate, LocalDate.now()).getYears();
            if (calculatedAge != newAge) {
                birthDate = LocalDate.now().minusYears(newAge);
            }
        } else if (birthDate == null && newAge != null) {
            birthDate = LocalDate.now().minusYears(newAge);
        } else if (birthDate != null && newAge == null) {
            newAge = Period.between(birthDate, LocalDate.now()).getYears();
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to update this student's data?", ButtonType.YES, ButtonType.NO);
        LocalDate finalBirthDate = birthDate;
        Integer finalNewAge = newAge;
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection conn = connectDB()) {
                    String getSql = "SELECT firstname, lastname, email, birthdate, age, gender FROM public.\"User\" WHERE email = ?";
                    String oldFirstname = null, oldLastname = null, oldEmail = null, oldGender = null;
                    LocalDate oldBirthDate = null;
                    Integer oldAge = null;
                    try (PreparedStatement getStmt = conn.prepareStatement(getSql)) {
                        getStmt.setString(1, selected.getEmail());
                        ResultSet rs = getStmt.executeQuery();
                        if (rs.next()) {
                            oldFirstname = rs.getString("firstname");
                            oldLastname = rs.getString("lastname");
                            oldEmail = rs.getString("email");
                            oldBirthDate = rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null;
                            oldAge = rs.getInt("age");
                            oldGender = rs.getString("gender");
                        }
                    }
                    if (!firstNameText.isEmpty()) oldFirstname = firstNameText;
                    if (!lastNameText.isEmpty()) oldLastname = lastNameText;
                    if (!emailText.isEmpty()) oldEmail = emailText;
                    if (finalBirthDate != null) oldBirthDate = finalBirthDate;
                    if (finalNewAge != null) oldAge = finalNewAge;
                    if (!gender.isEmpty()) oldGender = gender;
                    String sql = "UPDATE public.\"User\" SET firstname = ?, lastname = ?, email = ?, birthdate = ?, age = ?, gender = ? WHERE email = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, oldFirstname);
                        pstmt.setString(2, oldLastname);
                        pstmt.setString(3, oldEmail);
                        pstmt.setDate(4, oldBirthDate != null ? Date.valueOf(oldBirthDate) : null);
                        pstmt.setInt(5, oldAge);
                        pstmt.setString(6, oldGender);
                        pstmt.setString(7, selected.getEmail());
                        int updatedRows = pstmt.executeUpdate();
                        if (updatedRows > 0) {
                            new Alert(Alert.AlertType.INFORMATION, "Student data updated successfully", ButtonType.OK).showAndWait();
                            showAllStudents();
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "An error occurred while updating the data", ButtonType.OK).showAndWait();
                }
            }
        });
    }

    @FXML
    private void handleDeleteStudent() {
        StudentAbsence selected = tbl.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Please select a student to delete.",
                    ButtonType.OK).showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to permanently delete the student account for " + selected.getEmail() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (deleteStudent(selected.getEmail())) {
                    showAllStudents();
                    new Alert(Alert.AlertType.INFORMATION,
                            "Student account deleted successfully.",
                            ButtonType.OK).showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR,
                            "Failed to delete student account.",
                            ButtonType.OK).showAndWait();
                }
            }
        });
    }

    private boolean deleteStudent(String email) {
        String sql = "DELETE FROM public.\"User\" WHERE email = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAllStudents() {
        loadData1("");
        clearSearchFields();
    }

    @FXML
    private void saveStudent() {
        String firstName = addFirstname.getText().trim();
        String lastName = addLastname.getText().trim();
        String emailText = addEmail.getText().trim();
        String passwordText = addPassword.getText();
        LocalDate birthDate = addBirth.getValue();
        String gender = addMale.isSelected() ? "Male" : addFemale.isSelected() ? "Female" : "";

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || birthDate == null || gender.isEmpty()) {
            emaillabel.setText("All fields are required!");
            emaillabel.setVisible(true);
            return;
        }

        // Validate password length
        if (passwordText.length() < 8) {
            emaillabel.setText("Password must be at least 8 characters!");
            emaillabel.setVisible(true);
            return;
        }

        // Validate email format
        if (!emailText.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            emaillabel.setText("Email not valid");
            emaillabel.setVisible(true);
            return;
        }

        // Check if email already exists
        try (Connection conn = connectDB()) {
            String checkEmailSql = "SELECT email FROM public.\"User\" WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql)) {
                checkStmt.setString(1, emailText);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    emaillabel.setText("Email already exists!");
                    emaillabel.setVisible(true);
                    return;
                }
            }

            // Calculate age
            int age = Period.between(birthDate, LocalDate.now()).getYears();

            conn.setAutoCommit(false); // Start transaction

            // Insert into User table
            String userSql = "INSERT INTO public.\"User\" (firstname, lastname, email, gender, birthdate, age, password,user_type) VALUES (?, ?, ?, ?, ?, ?, ?,1)";
            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                userStmt.setString(1, firstName);
                userStmt.setString(2, lastName);
                userStmt.setString(3, emailText);
                userStmt.setString(4, gender);
                userStmt.setDate(5, java.sql.Date.valueOf(birthDate));
                userStmt.setInt(6, age);
                userStmt.setString(7, passwordText);
                userStmt.executeUpdate();
            }

            // Insert into student table
            String studentSql = "INSERT INTO public.student (email) VALUES (?)";
            try (PreparedStatement studentStmt = conn.prepareStatement(studentSql)) {
                studentStmt.setString(1, emailText);
                studentStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            clearAddStudentFields();
            emaillabel.setVisible(false);
            new Alert(Alert.AlertType.INFORMATION, "Student added successfully!", ButtonType.OK).showAndWait();

        } catch (SQLException e) {
            emaillabel.setText("Error: " + e.getMessage());
            emaillabel.setVisible(true);
            try (Connection conn = connectDB()) {
                conn.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void clearAddStudentFields() {
        addFirstname.clear();
        addLastname.clear();
        addEmail.clear();
        addPassword.clear();
        addBirth.setValue(null);
        addTow.selectToggle(null);
        emaillabel.setVisible(false);
    }

    @FXML
    private void saveTeacher() {
        String firstName = teacherFirstname.getText().trim();
        String lastName = teacherLastname.getText().trim();
        String emailText = teacherEmail.getText().trim();
        String passwordText = teacherPasswordField.getText().trim();
        String salaryText = teacherSalary.getText().trim();
        LocalDate birthDate = teacherBirth.getValue();
        String gender = teacherMale.isSelected() ? "Male" : teacherFemale.isSelected() ? "Female" : "";

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || 
            salaryText.isEmpty() || birthDate == null || gender.isEmpty()) {
            teacherEmaillabel.setText("All fields are required!");
            teacherEmaillabel.setVisible(true);
            return;
        }

        // Validate password length
        if (passwordText.length() < 8) {
            teacherEmaillabel.setText("Password must be at least 8 characters!");
            teacherEmaillabel.setVisible(true);
            return;
        }

        // Validate email format
        if (!emailText.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            teacherEmaillabel.setText("Email not valid");
            teacherEmaillabel.setVisible(true);
            return;
        }

        // Validate salary
        double salaryValue;
        try {
            salaryValue = Double.parseDouble(salaryText);
            if (salaryValue <= 0) {
                teacherEmaillabel.setText("Salary must be a positive number!");
                teacherEmaillabel.setVisible(true);
                return;
            }
        } catch (NumberFormatException e) {
            teacherEmaillabel.setText("Invalid salary format!");
            teacherEmaillabel.setVisible(true);
            return;
        }

        // Check if email already exists
        try (Connection conn = connectDB()) {
            String checkEmailSql = "SELECT email FROM public.\"User\" WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql)) {
                checkStmt.setString(1, emailText);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    teacherEmaillabel.setText("Email already exists!");
                    teacherEmaillabel.setVisible(true);
                    return;
                }
            }

            // Calculate age
            int age = Period.between(birthDate, LocalDate.now()).getYears();

            conn.setAutoCommit(false); // Start transaction

            // Insert into User table
            String userSql = "INSERT INTO public.\"User\" (firstname, lastname, email, gender, birthdate, age, password,user_type) VALUES (?, ?, ?, ?, ?, ?, ?,2)";
            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                userStmt.setString(1, firstName);
                userStmt.setString(2, lastName);
                userStmt.setString(3, emailText);
                userStmt.setString(4, gender);
                userStmt.setDate(5, java.sql.Date.valueOf(birthDate));
                userStmt.setInt(6, age);
                userStmt.setString(7, passwordText);
                userStmt.executeUpdate();
            }

            // Insert into teacher table
            String teacherSql = "INSERT INTO public.teacher (email, salary) VALUES (?, ?)";
            try (PreparedStatement teacherStmt = conn.prepareStatement(teacherSql)) {
                teacherStmt.setString(1, emailText);
                teacherStmt.setDouble(2, salaryValue);
                teacherStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            clearAddTeacherFields();
            teacherEmaillabel.setVisible(false);
            new Alert(Alert.AlertType.INFORMATION, "Teacher added successfully!", ButtonType.OK).showAndWait();

        } catch (SQLException e) {
            teacherEmaillabel.setText("Error: " + e.getMessage());
            teacherEmaillabel.setVisible(true);
            try (Connection conn = connectDB()) {
                conn.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void clearAddTeacherFields() {
        teacherFirstname.clear();
        teacherLastname.clear();
        teacherEmail.clear();
        teacherPasswordField.clear();
        teacherSalary.clear();
        teacherBirth.setValue(null);
        teacherTow.selectToggle(null);
        teacherEmaillabel.setVisible(false);
    }


    // Teacher search methods
    private void loadTeachersFromDB() {
        teacherList.clear();
        try (Connection conn = connectDB()) {
            String query = "SELECT u.firstname, u.lastname, u.email, u.gender, u.birthdate, u.age, t.salary " +
                          "FROM public.\"User\" u JOIN public.teacher t ON u.email = t.email";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                teacherList.add(new Teacher(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null,
                        rs.getInt("age"),
                        rs.getDouble("salary")
                ));
            }
            tblt.setItems(teacherList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading teachers: " + e.getMessage()).showAndWait();
        }
    }

    private void clearTeacherFields() {
        firstnamet.clear();
        lastnamet.clear();
        emailt.clear();
        salaryt.clear();
        aget.clear();
        birtht.setValue(null);
        malet.setSelected(false);
        femalet.setSelected(false);
    }

    @FXML
    private void searchTeacher() {
        Teacher selectedTeacher = tblt.getSelectionModel().getSelectedItem();
        teacherList.clear();
        if (selectedTeacher != null) {
            try (Connection conn = connectDB()) {
                String query = "SELECT u.firstname, u.lastname, u.email, u.gender, u.birthdate, u.age, t.salary " +
                              "FROM public.\"User\" u JOIN public.teacher t ON u.email = t.email WHERE u.email = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, selectedTeacher.getEmail());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    teacherList.add(new Teacher(
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("email"),
                            rs.getString("gender"),
                            rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null,
                            rs.getInt("age"),
                            rs.getDouble("salary")
                    ));
                }
                tblt.setItems(teacherList);
                if (teacherList.isEmpty()) {
                    new Alert(Alert.AlertType.INFORMATION, "No matching teacher found.").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error searching teachers: " + e.getMessage()).showAndWait();
            }
            clearTeacherFields();
            return;
        }

        String firstnameText = firstnamet.getText().trim();
        String lastnameText = lastnamet.getText().trim();
        String emailText = emailt.getText().trim();
        String salaryText = salaryt.getText().trim();
        String ageText = aget.getText().trim();
        LocalDate birthDate = birtht.getValue();
        String gender = malet.isSelected() ? "Male" : (femalet.isSelected() ? "Female" : "");

        if (firstnameText.isEmpty() && lastnameText.isEmpty() && emailText.isEmpty() && 
            salaryText.isEmpty() && ageText.isEmpty() && birthDate == null && gender.isEmpty()) {
            loadTeachersFromDB();
            clearTeacherFields();
            return;
        }

        StringBuilder whereClause = new StringBuilder();
        if (!firstnameText.isEmpty()) {
            whereClause.append(" AND u.firstname ILIKE '%").append(firstnameText).append("%'");
        }
        if (!lastnameText.isEmpty()) {
            whereClause.append(" AND u.lastname ILIKE '%").append(lastnameText).append("%'");
        }
        if (!emailText.isEmpty()) {
            whereClause.append(" AND u.email ILIKE '%").append(emailText).append("%'");
        }
        if (!salaryText.isEmpty()) {
            try {
                Double.parseDouble(salaryText);
                whereClause.append(" AND t.salary = ").append(salaryText);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Please enter a valid salary.").showAndWait();
                return;
            }
        }
        if (!ageText.isEmpty()) {
            try {
                Integer.parseInt(ageText);
                whereClause.append(" AND u.age = ").append(ageText);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Please enter a valid age.").showAndWait();
                return;
            }
        }
        if (birthDate != null) {
            whereClause.append(" AND u.birthdate = '").append(Date.valueOf(birthDate)).append("'");
        }
        if (!gender.isEmpty()) {
            whereClause.append(" AND u.gender = '").append(gender).append("'");
        }

        String query = "SELECT u.firstname, u.lastname, u.email, u.gender, u.birthdate, u.age, t.salary " +
                      "FROM public.\"User\" u JOIN public.teacher t ON u.email = t.email WHERE 1=1" + 
                      whereClause + " ORDER BY u.firstname, u.lastname";
        try (Connection conn = connectDB()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                teacherList.add(new Teacher(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null,
                        rs.getInt("age"),
                        rs.getDouble("salary")
                ));
            }
            tblt.setItems(teacherList);
            if (teacherList.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No matching teachers found.").showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error searching teachers: " + e.getMessage()).showAndWait();
        }
        clearTeacherFields();
    }

    @FXML
    private void removeTeacher() {
        Teacher selectedTeacher = tblt.getSelectionModel().getSelectedItem();
        if (selectedTeacher == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a teacher to delete.").showAndWait();
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this teacher?",
                ButtonType.YES, ButtonType.NO);

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection conn = connectDB()) {
                    conn.setAutoCommit(false);

                    // احذف من جدول teacher
                    String deleteTeacher = "DELETE FROM public.teacher WHERE email = ?";
                    try (PreparedStatement pstmtTeacher = conn.prepareStatement(deleteTeacher)) {
                        pstmtTeacher.setString(1, selectedTeacher.getEmail());
                        pstmtTeacher.executeUpdate();
                    }

                    // احذف من جدول User
                    String deleteUser = "DELETE FROM public.\"User\" WHERE email = ?";
                    try (PreparedStatement pstmtUser = conn.prepareStatement(deleteUser)) {
                        pstmtUser.setString(1, selectedTeacher.getEmail());
                        pstmtUser.executeUpdate();
                    }

                    conn.commit();

                    new Alert(Alert.AlertType.INFORMATION, "Teacher deleted successfully.").showAndWait();
                    loadTeachersFromDB();
                    tblt.refresh();

                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        Connection conn = connectDB();
                        conn.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    new Alert(Alert.AlertType.ERROR, "Error deleting teacher: " + e.getMessage()).showAndWait();
                }
            }
        });
    }

    @FXML
    private void updateTeacher() {
        Teacher selectedTeacher = tblt.getSelectionModel().getSelectedItem();
        if (selectedTeacher == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a row to update.").showAndWait();
            return;
        }

        String firstnameText = firstnamet.getText().trim();
        String lastnameText = lastnamet.getText().trim();
        String salaryText = salaryt.getText().trim();
        String ageText = aget.getText().trim();
        LocalDate birthDate = birtht.getValue();
        String gender = malet.isSelected() ? "Male" : (femalet.isSelected() ? "Female" : "");

        if (firstnameText.isEmpty() && lastnameText.isEmpty() && salaryText.isEmpty() && ageText.isEmpty() && birthDate == null && gender.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill at least one field to update.").showAndWait();
            return;
        }

        // Validate salary
        Double newSalary = null;
        if (!salaryText.isEmpty()) {
            try {
                newSalary = Double.parseDouble(salaryText);
                if (newSalary <= 0) {
                    new Alert(Alert.AlertType.ERROR, "Salary must be a positive number.").showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Salary must be a valid number.").showAndWait();
                return;
            }
        }

        // Synchronize age and birthdate
        Integer newAge = null;
        if (!ageText.isEmpty()) {
            try {
                newAge = Integer.parseInt(ageText);
                if (newAge <= 0) {
                    new Alert(Alert.AlertType.ERROR, "Age must be a positive integer.").showAndWait();
                    return;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Age must be a valid integer.").showAndWait();
                return;
            }
        }
        if (birthDate != null && newAge != null) {
            int calculatedAge = Period.between(birthDate, LocalDate.now()).getYears();
            if (calculatedAge != newAge) {
                birthDate = LocalDate.now().minusYears(newAge);
            }
        } else if (birthDate == null && newAge != null) {
            birthDate = LocalDate.now().minusYears(newAge);
        } else if (birthDate != null && newAge == null) {
            newAge = Period.between(birthDate, LocalDate.now()).getYears();
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to update this teacher's data?", ButtonType.YES, ButtonType.NO);
        LocalDate finalBirthDate = birthDate;
        Integer finalNewAge = newAge;
        Double finalNewSalary = newSalary;
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection conn = connectDB()) {
                    conn.setAutoCommit(false);

                    // Fetch current data
                    String getSql = "SELECT u.firstname, u.lastname, u.email, u.birthdate, u.age, u.gender, t.salary " +
                                   "FROM public.\"User\" u JOIN public.teacher t ON u.email = t.email WHERE u.email = ?";
                    String oldFirstname = null, oldLastname = null, oldEmail = null, oldGender = null;
                    LocalDate oldBirthDate = null;
                    Integer oldAge = null;
                    Double oldSalary = null;
                    try (PreparedStatement getStmt = conn.prepareStatement(getSql)) {
                        getStmt.setString(1, selectedTeacher.getEmail());
                        ResultSet rs = getStmt.executeQuery();
                        if (rs.next()) {
                            oldFirstname = rs.getString("firstname");
                            oldLastname = rs.getString("lastname");
                            oldEmail = rs.getString("email");
                            oldBirthDate = rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null;
                            oldAge = rs.getInt("age");
                            oldGender = rs.getString("gender");
                            oldSalary = rs.getDouble("salary");
                        }
                    }

                    // Update fields only if new values are provided
                    if (!firstnameText.isEmpty()) oldFirstname = firstnameText;
                    if (!lastnameText.isEmpty()) oldLastname = lastnameText;
                    if (finalBirthDate != null) oldBirthDate = finalBirthDate;
                    if (finalNewAge != null) oldAge = finalNewAge;
                    if (!gender.isEmpty()) oldGender = gender;
                    if (finalNewSalary != null) oldSalary = finalNewSalary;

                    // Update User table (email remains unchanged)
                    String updateUserSql = "UPDATE public.\"User\" SET firstname = ?, lastname = ?, birthdate = ?, age = ?, gender = ? WHERE email = ?";
                    try (PreparedStatement pstmtUser = conn.prepareStatement(updateUserSql)) {
                        pstmtUser.setString(1, oldFirstname);
                        pstmtUser.setString(2, oldLastname);
                        pstmtUser.setDate(3, oldBirthDate != null ? Date.valueOf(oldBirthDate) : null);
                        pstmtUser.setInt(4, oldAge != null ? oldAge : 0);
                        pstmtUser.setString(5, oldGender);
                        pstmtUser.setString(6, selectedTeacher.getEmail());
                        int updatedRows = pstmtUser.executeUpdate();
                        if (updatedRows == 0) {
                            throw new SQLException("No rows updated in User table.");
                        }
                    }

                    // Update teacher table
                    String updateTeacherSql = "UPDATE public.teacher SET salary = ? WHERE email = ?";
                    try (PreparedStatement pstmtTeacher = conn.prepareStatement(updateTeacherSql)) {
                        pstmtTeacher.setDouble(1, oldSalary != null ? oldSalary : 0.0);
                        pstmtTeacher.setString(2, selectedTeacher.getEmail());
                        int updatedRows = pstmtTeacher.executeUpdate();
                        if (updatedRows == 0) {
                            throw new SQLException("No rows updated in teacher table.");
                        }
                    }

                    conn.commit();
                    new Alert(Alert.AlertType.INFORMATION, "Teacher data updated successfully.").showAndWait();
                    loadTeachersFromDB();
                    clearTeacherFields();

                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error updating teacher: " + e.getMessage()).showAndWait();
                }
            }
        });
    }
    
 

    @Override
    protected void finalize() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}