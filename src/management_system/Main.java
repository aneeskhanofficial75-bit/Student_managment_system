package management_system;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Admin admin = new Admin();
        int choice = 0;

        do {
            System.out.println("\n=================================");
            System.out.println("         Student/Teacher/Admin LOGIN SYSTEM");
            System.out.println("=================================");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("\nEnter your choice: ");
            if (!input.hasNextInt()) {
                System.out.println("Invalid choice. Enter a number.");
                input.nextLine();
                continue;
            }

            choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                System.out.println("\n=================================");
                System.out.println("       SELECT ROLE FOR SIGN UP");
                System.out.println("=================================");
                System.out.println("1. Student");
                System.out.println("2. Teacher");
                System.out.print("\nEnter your choice: ");

                if (!input.hasNextInt()) {
                    System.out.println("Invalid choice.");
                    input.nextLine();
                    continue;
                }

                int signUpRole = input.nextInt();
                input.nextLine();

                if (signUpRole == 1) {

                    System.out.println("\n========== STUDENT SIGN UP ==========");
                    System.out.print("Enter Name: ");
                    String name = input.nextLine();

                    System.out.print("Enter Email: ");
                    String email = input.nextLine();

                    System.out.print("Enter Department: ");
                    String department = input.nextLine();

                    System.out.print("Create Password at least 6 characters: ");
                    String password = input.nextLine();
                    if (password.length() < 6) {
                        System.out.println("Password must be at least 6 characters.");
                        continue;
                    }

                    System.out.println("\nRegistering student to Supabase & Generating User UID...");

                    boolean registered = SupabaseService.signUp(email, password, name, department);
                    if (registered) {
                        System.out.println("\n=================================");
                        System.out.println("       SIGN UP SUCCESSFUL");
                        System.out.println("=================================");
                        System.out.println("Student account created successfully with Supabase UID.");
                    } else {
                        System.out.println("\nStudent Sign Up failed.");
                    }
                } else if (signUpRole == 2) {

                    System.out.println("\n========== TEACHER SIGN UP ==========");
                    System.out.print("Enter Teacher Name: ");
                    String name = input.nextLine();

                    System.out.print("Enter Department: ");
                    String department = input.nextLine();

                    System.out.print("Enter Email: ");
                    String email = input.nextLine();

                    System.out.print("Create Password: ");
                    String teacherPassword = input.nextLine();

                    if (teacherPassword.length() < 6) {
                        System.out.println("Password must be at least 6 characters");
                        continue;
                    }

                    System.out.println("\nRegistering teacher to Supabase & Generating User UID...");

                    boolean registered = SupabaseService.teacherSignUp(email, teacherPassword, name, department);
                    if (registered) {
                        System.out.println("SIGN UP SUCCESSFUL");
                        System.out.println("Teacher account created successfully with Supabase UID.");
                    } else {
                        System.out.println("\nTeacher Sign Up failed.");
                    }
                } else {
                    System.out.println("\nInvalid Role selected.");
                }
            } else if (choice == 2) {
                System.out.println("\n=================================");
                System.out.println("              LOGIN");
                System.out.println("=================================");
                System.out.print("Enter Email or User UID: ");
                String inputIdentifier = input.nextLine().trim();
                System.out.print("Enter Password: ");
                String password = input.nextLine();

                String email = SupabaseService.getEmailByUidFromCloud(inputIdentifier);

                if (admin.login(email, password)) {
                    System.out.println("\n=================================");
                    System.out.println("       LOGIN SUCCESSFUL (ADMIN)");
                    System.out.println("=================================");
                    System.out.println("Welcome Admin");

                    int adminChoice = 0;
                    do {
                        System.out.println("\n=================================");
                        System.out.println("      STUDENT MANAGEMENT SYSTEM");
                        System.out.println("=================================");
                        System.out.println("1. View All Students");
                        System.out.println("2. Search Student by UID");
                        System.out.println("3. Update Student");
                        System.out.println("4. Delete Student");
                        System.out.println("5. View All Teachers");
                        System.out.println("6. Search Teacher by UID");
                        System.out.println("7. Assign Course to Teacher");
                        System.out.println("8. Manage Available Courses ");
                        System.out.println("9. Logout");
                        System.out.print("\nEnter your choice: ");

                        if (!input.hasNextInt()) {
                            System.out.println("Invalid choice.");
                            input.nextLine();
                            continue;
                        }

                        adminChoice = input.nextInt();
                        input.nextLine();

                        switch (adminChoice) {
                            case 1:
                                System.out.println("\nLoading students from Supabase...");
                                admin.viewAllStudents();
                                break;

                            case 2:
                                System.out.println("\n======= SEARCH STUDENT =======");
                                System.out.print("Enter Student UID: ");
                                String searchId = input.nextLine();
                                admin.searchStudent(searchId);
                                break;

                            case 3:
                                System.out.println("\n======= UPDATE STUDENT =======");
                                System.out.print("Enter Student UID: ");
                                String updateId = input.nextLine();
                                Student existingStudent = admin.findStudent(updateId);
                                if (existingStudent == null) {
                                    admin.loadStudentsFromCloud();
                                    existingStudent = admin.findStudent(updateId);
                                }
                                if (existingStudent == null) {
                                    System.out.println("Student not found.");
                                    break;
                                }

                                System.out.println("\nCurrent Student Information:");
                                existingStudent.displayInfo();

                                System.out.print("Enter New Name: ");
                                String newName = input.nextLine();
                                System.out.print("Enter New Department: ");

                                String newDepartment = input.nextLine();
                                System.out.print("Enter New CGPA: ");

                                if (!input.hasNextDouble()) {
                                    System.out.println("Invalid CGPA.");
                                    input.nextLine();
                                    break;
                                }
                                double newCgpa = input.nextDouble();
                                input.nextLine();

                                if (admin.updateStudent(updateId, newName, newDepartment, newCgpa)) {
                                    System.out.println("\nStudent updated successfully!");
                                } else {
                                    System.out.println("\nStudent update failed.");
                                }
                                break;

                            case 4:
                                System.out.println("\n======= DELETE STUDENT =======");
                                System.out.print("Enter Student UID: ");
                                String deleteId = input.nextLine();
                                Student studentToDelete = admin.findStudent(deleteId);
                                if (studentToDelete == null) {
                                    admin.loadStudentsFromCloud();
                                    studentToDelete = admin.findStudent(deleteId);
                                }
                                if (studentToDelete == null) {
                                    System.out.println("Student not found.");
                                    break;
                                }

                                System.out.println("\nStudent to delete:");
                                studentToDelete.displayInfo();
                                System.out.print("Are you sure? (yes/no): ");
                                String confirm = input.nextLine();

                                if (confirm.equalsIgnoreCase("yes")) {
                                    if (admin.deleteStudent(deleteId)) {
                                        System.out.println("\nStudent deleted successfully!");
                                    } else {
                                        System.out.println("\nStudent deletion failed.");
                                    }
                                } else {
                                    System.out.println("Delete cancelled.");
                                }
                                break;

                            case 5:
                                System.out.println("\nLoading teachers from Supabase");
                                admin.viewAllTeachers();
                                break;

                            case 6:
                                System.out.println("\n======= SEARCH TEACHER =======");
                                System.out.print("Enter Teacher UID: ");
                                String teacherId = input.nextLine();
                                admin.searchTeacher(teacherId);
                                break;

                            case 7:
                                System.out.println("\n======= ASSIGN COURSE TO TEACHER =======");
                                String teachersJson = SupabaseService.getAllTeachersFromCloud();
                                List<String> teacherList = SupabaseService.extractJsonObjects(teachersJson);

                                if (teacherList.isEmpty()) {
                                    System.out.println("No registered teachers found in database.");
                                    break;
                                }

                                System.out.println("\n=================================");
                                System.out.println("       REGISTERED TEACHERS");
                                System.out.println("=================================");
                                for (int i = 0; i < teacherList.size(); i++) {
                                    String tRec = teacherList.get(i);
                                    String tName = SupabaseService.getString(tRec, "name");
                                    String tEmail = SupabaseService.getString(tRec, "email");
                                    String tDept = SupabaseService.getString(tRec, "department");
                                    System.out.println((i + 1) + ". " + tName + " [" + tEmail + "] (" + tDept + ")");
                                }
                                System.out.println("=================================");
                                System.out.print("Select Teacher Number: ");
                                if (!input.hasNextInt()) {
                                    System.out.println("Invalid input.");
                                    input.nextLine();
                                    break;
                                }
                                int tChoice = input.nextInt();
                                input.nextLine();

                                if (tChoice < 1 || tChoice > teacherList.size()) {
                                    System.out.println("Invalid teacher selection.");
                                    break;
                                }

                                String selectedTeacherRec = teacherList.get(tChoice - 1);
                                String selTeacherId = SupabaseService.getString(selectedTeacherRec, "id");
                                String selTeacherEmail = SupabaseService.getString(selectedTeacherRec, "email");
                                String selTeacherName = SupabaseService.getString(selectedTeacherRec, "name");

                                String coursesJsonAdmin = SupabaseService.getAllCoursesFromCloud();
                                List<String> courseListAdmin = SupabaseService.extractJsonObjects(coursesJsonAdmin);

                                if (courseListAdmin.isEmpty()) {
                                    System.out.println("\nNo available courses found. Admin must add courses first.");
                                    break;
                                }

                                System.out.println("\n=================================");
                                System.out.println("       AVAILABLE COURSES");
                                System.out.println("=================================");
                                for (int i = 0; i < courseListAdmin.size(); i++) {
                                    String cRec = courseListAdmin.get(i);
                                    int cSem = SupabaseService.getInt(cRec, "semester");
                                    String cCode = SupabaseService.getString(cRec, "course_code");
                                    String cName = SupabaseService.getString(cRec, "course_name");
                                    System.out.println((i + 1) + ". [Semester " + cSem + "] [" + cCode + "] " + cName);
                                }
                                System.out.println("=================================");
                                System.out.print("Select Course Number to Assign: ");
                                if (!input.hasNextInt()) {
                                    System.out.println("Invalid input.");
                                    input.nextLine();
                                    break;
                                }
                                int cChoice = input.nextInt();
                                input.nextLine();

                                if (cChoice < 1 || cChoice > courseListAdmin.size()) {
                                    System.out.println("Invalid course selection.");
                                    break;
                                }

                                String selectedCourseRec = courseListAdmin.get(cChoice - 1);
                                String assignCode = SupabaseService.getString(selectedCourseRec, "course_code");
                                String assignName = SupabaseService.getString(selectedCourseRec, "course_name");

                                boolean assigned = SupabaseService.assignCourseToTeacherInCloud(selTeacherId, selTeacherEmail, assignCode, assignName);
                                if (assigned) {
                                    System.out.println("\nCourse [" + assignCode + "] " + assignName + " successfully assigned to Teacher " + selTeacherName + "!");
                                } else {
                                    System.out.println("\nFailed to assign course.");
                                }
                                break;

                            case 8:
                                int courseAdminChoice = 0;
                                do {
                                    System.out.println("\n=================================");
                                    System.out.println("       COURSE MANAGEMENT");
                                    System.out.println("=================================");
                                    System.out.println("1. Add Course to Semester");
                                    System.out.println("2. View All Available Courses");
                                    System.out.println("3. Delete Course");
                                    System.out.println("4. Back to Main Admin Menu");
                                    System.out.print("\nEnter choice: ");

                                    if (!input.hasNextInt()) {
                                        System.out.println("Invalid choice.");
                                        input.nextLine();
                                        continue;
                                    }
                                    courseAdminChoice = input.nextInt();
                                    input.nextLine();

                                    switch (courseAdminChoice) {
                                        case 1:
                                            System.out.print("Enter Semester Number (1 to 8): ");
                                            if (!input.hasNextInt()) {
                                                System.out.println("Invalid Semester Number.");
                                                input.nextLine();
                                                break;
                                            }
                                            int sem = input.nextInt();
                                            input.nextLine();

                                            if (sem < 1 || sem > 8) {
                                                System.out.println("Invalid Semester Number. Semester must be between 1 and 8.");
                                                break;
                                            }

                                            System.out.print("Enter Course Code (eg CS101): ");
                                            String cCode = input.nextLine();
                                            System.out.print("Enter Course Name: ");
                                            String cName = input.nextLine();
                                            System.out.print("Enter Department: ");
                                            String cDept = input.nextLine();

                                            boolean added = SupabaseService.addCourseToCloud(cCode, cName, sem, cDept);
                                            if (added) {
                                                System.out.println("\nCourse added successfully for Semester " + sem);
                                            } else {
                                                System.out.println("\nFailed to add course.");
                                            }
                                            break;

                                        case 2:
                                            String allCoursesJson = SupabaseService.getAllCoursesFromCloud();
                                            List<String> courseList = SupabaseService.extractJsonObjects(allCoursesJson);
                                            if (courseList.isEmpty()) {
                                                System.out.println("\nNo courses found in database.");
                                            } else {
                                                System.out.println("\n=================================");
                                                System.out.println("      ALL AVAILABLE COURSES");
                                                System.out.println("=================================");
                                                for (String cRec : courseList) {
                                                    int cId = SupabaseService.getInt(cRec, "id");
                                                    int cSem = SupabaseService.getInt(cRec, "semester");
                                                    String codeStr = SupabaseService.getString(cRec, "course_code");
                                                    String nameStr = SupabaseService.getString(cRec, "course_name");
                                                    String deptStr = SupabaseService.getString(cRec, "department");
                                                    System.out.println("ID: " + cId + " | Semester " + cSem + " | [" + codeStr + "] " + nameStr + " (" + deptStr + ")");
                                                }
                                                System.out.println("=================================");
                                            }
                                            break;

                                        case 3:
                                            System.out.print("Enter Course ID to delete: ");
                                            if (!input.hasNextInt()) {
                                                System.out.println("Invalid ID.");
                                                input.nextLine();
                                                break;
                                            }
                                            int delId = input.nextInt();
                                            input.nextLine();
                                            if (SupabaseService.deleteCourseFromCloud(delId)) {
                                                System.out.println("\nCourse deleted successfully!");
                                            } else {
                                                System.out.println("\nFailed to delete course.");
                                            }
                                            break;

                                        case 4:
                                            System.out.println("Returning to Admin Menu...");
                                            break;

                                        default:
                                            System.out.println("Invalid choice.");
                                    }
                                } while (courseAdminChoice != 4);
                                break;

                            case 9:
                                System.out.println("\nAdmin Logged Out.");
                                break;

                            default:
                                System.out.println("\nInvalid choice.");
                        }
                    } while (adminChoice != 9);

                } else {
                    System.out.println("\nVerifying credentials with Supabase");
                    if (SupabaseService.login(email, password)) {

                        String studentProfile = SupabaseService.getStudentProfile(email);
                        List<String> studentObjs = SupabaseService.extractJsonObjects(studentProfile);

                        if (!studentObjs.isEmpty()) {

                            System.out.println("\n=================================");
                            System.out.println("    LOGIN SUCCESSFUL STUDENT");
                            System.out.println("=================================");
                            System.out.println("Welcome, " + email);

                            System.out.println("\n========== YOUR PROFILE DETAILS ==========");
                            SupabaseService.displayFormattedProfile(studentProfile, "Student");

                            String studentId = SupabaseService.getString(studentObjs.get(0), "id");

                            int studentChoice = 0;
                            do {
                                System.out.println("\n=================================");
                                System.out.println("        STUDENT DASHBOARD");
                                System.out.println("=================================");
                                System.out.println("1. View My Enrolled Courses");
                                System.out.println("2. Register Courses for a Semester");
                                System.out.println("3. Re-register Semester Courses");
                                System.out.println("4. Logout");
                                System.out.print("\nEnter your choice: ");

                                if (!input.hasNextInt()) {
                                    System.out.println("Invalid choice.");
                                    input.nextLine();
                                    continue;
                                }

                                studentChoice = input.nextInt();
                                input.nextLine();

                                switch (studentChoice) {
                                    case 1:
                                        String enrolledJson = SupabaseService.getStudentEnrolledCoursesFromCloud(email);
                                        SupabaseService.displayFormattedStudentCourses(enrolledJson);
                                        break;

                                    case 2:
                                    case 3:
                                        System.out.print("\nEnter Semester Number (1 to 8): ");
                                        if (!input.hasNextInt()) {
                                            System.out.println("Invalid Semester Number.");
                                            input.nextLine();
                                            break;
                                        }
                                        int semester = input.nextInt();
                                        input.nextLine();

                                        if (semester < 1 || semester > 8) {
                                            System.out.println("Invalid Semester Number. Semester must be between 1 and 8.");
                                            break;
                                        }

                                        if (studentChoice == 3) {
                                            System.out.print("Are you sure you want to edit courses for Semester " + semester + "? (yes/no): ");
                                            String confirm = input.nextLine();
                                            if (!confirm.equalsIgnoreCase("yes")) {
                                                System.out.println("Edit cancelled.");
                                                break;
                                            }
                                            SupabaseService.deleteStudentEnrolledCoursesInCloud(email, semester);
                                            System.out.println("Previous course selections for Semester " + semester + " cleared.");
                                        }

                                        String coursesJson = SupabaseService.getCoursesBySemesterFromCloud(semester);
                                        List<String> availableCourses = SupabaseService.extractJsonObjects(coursesJson);

                                        if (availableCourses.isEmpty()) {
                                            System.out.println("\nNo courses available for Semester " + semester + " yet. Please ask Admin to add courses.");
                                            break;
                                        }

                                        System.out.println("\n=================================");
                                        System.out.println(" AVAILABLE COURSES FOR SEMESTER " + semester);
                                        System.out.println("=================================");
                                        for (int i = 0; i < availableCourses.size(); i++) {
                                            String cRecord = availableCourses.get(i);
                                            String cCode = SupabaseService.getString(cRecord, "course_code");
                                            String cName = SupabaseService.getString(cRecord, "course_name");

                                            int enrolledCount = SupabaseService.getEnrolledStudentCountForCourse(cCode);
                                            String statusStr = enrolledCount >= 50 ? "[FULL - 50/50]" : "(" + enrolledCount + "/50 Enrolled)";
                                            System.out.println((i + 1) + ". [" + cCode + "] " + cName + " " + statusStr);
                                        }
                                        System.out.println("=================================");
                                        System.out.println("Enter course number to add (or type 'done' to finish):");

                                        boolean courseAdded = false;
                                        while (true) {
                                            System.out.print("Select Course # (or 'done'): ");
                                            String selInput = input.nextLine().trim();
                                            if (selInput.equalsIgnoreCase("done")) {
                                                break;
                                            }
                                            try {
                                                int courseNum = Integer.parseInt(selInput);
                                                if (courseNum >= 1 && courseNum <= availableCourses.size()) {
                                                    String selCourse = availableCourses.get(courseNum - 1);
                                                    String cCode = SupabaseService.getString(selCourse, "course_code");
                                                    String cName = SupabaseService.getString(selCourse, "course_name");

                                                    int enrolledCount = SupabaseService.getEnrolledStudentCountForCourse(cCode);
                                                    if (enrolledCount >= 50) {
                                                        System.out.println("  Course [" + cCode + "] " + cName + " is FULL! (Limit of 50 students reached: " + enrolledCount + "/50).");
                                                        continue;
                                                    }

                                                    boolean ok = SupabaseService.enrollStudentCourseInCloud(studentId, email, semester, cCode, cName);
                                                    if (ok) {
                                                        System.out.println(" -> Added: " + cName + " (" + (enrolledCount + 1) + "/50 Enrolled)");
                                                        courseAdded = true;
                                                    } else {
                                                        System.out.println(" -> Failed to add course.");
                                                    }
                                                } else {
                                                    System.out.println("Invalid number. Choose between 1 and " + availableCourses.size());
                                                }
                                            } catch (NumberFormatException e) {
                                                System.out.println("Please enter a valid number or 'done'.");
                                            }
                                        }

                                        if (courseAdded) {
                                            System.out.println("\nSemester " + semester + " courses registered successfully in Supabase!");
                                        }
                                        break;

                                    case 4:
                                        System.out.println("\nStudent Logged Out.");
                                        break;

                                    default:
                                        System.out.println("\nInvalid choice.");
                                }
                            } while (studentChoice != 4);

                        } else {

                            String teacherProfile = SupabaseService.getTeacherProfile(email);
                            List<String> teacherObjs = SupabaseService.extractJsonObjects(teacherProfile);

                            if (!teacherObjs.isEmpty()) {

                                System.out.println("\n=================================");
                                System.out.println("    LOGIN SUCCESSFUL TEACHER");
                                System.out.println("=================================");
                                System.out.println("Welcome, " + email);
                                System.out.println("\n========== YOUR PROFILE DETAILS ==========");
                                SupabaseService.displayFormattedProfile(teacherProfile, "Teacher");

                                String teacherAssignedCourses = SupabaseService.getTeacherAssignedCoursesFromCloud(email);
                                SupabaseService.displayFormattedTeacherCourses(teacherAssignedCourses);

                            } else {
                                System.out.println("\nLogin successful, but profile record not found in Students or Teachers database.");
                            }
                        }

                    } else {
                        System.out.println("\nInvalid Email or Password.");
                    }
                }
            } else if (choice == 3) {
                System.out.println("\nProgram Ended");
            } else {
                System.out.println("\nInvalid choice. Enter 1-3");
            }

        } while (choice != 3);

        input.close();
    }
}
