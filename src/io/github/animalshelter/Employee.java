package io.github.animalshelter;

/**
 * This class allows the creation of Employee objects.
 *
 * @author Dalton Senseman
 * @author Jeff Munoz
 * @author Jean Paul Mathew
 * @author Tomas Vergara
 * @author William Ramanand
 */
public class Employee {

    private int employeeNum;
    private String firstName;
    private String lastName;
    private String jobClass;
    private String phoneNumber;
    private int payRate;
    private String hireDate;
    private String assignedTask;
    private String username;
    private String password;

    public Employee(int employeeNum, String firstName, String lastName, String jobClass, String phoneNumber, int payRate, String hireDate, String assignedTask) {
        this.employeeNum = employeeNum;
        this.firstName= firstName;
        this.lastName = lastName;
        this.jobClass = jobClass;
        this.phoneNumber = phoneNumber;
        this.payRate = payRate;
        this.hireDate = hireDate;
        this.assignedTask = assignedTask;
    }

    public Employee(int employeeNum, String firstName, String lastName, String jobClass, String phoneNumber, int payRate, String hireDate, String username, String password) {
        this.employeeNum = employeeNum;
        this.firstName= firstName;
        this.lastName = lastName;
        this.jobClass = jobClass;
        this.phoneNumber = phoneNumber;
        this.payRate = payRate;
        this.hireDate = hireDate;
        this.assignedTask = assignedTask;
        this.username = username;
        this.password = password;
    }

    public int getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(int employeeNum) {
        this.employeeNum = employeeNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getAssignedTask() {
        return assignedTask;
    }

    public void setAssignedTask(String assignedTask) {
        this.assignedTask = assignedTask;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPayRate() {
        return payRate;
    }

    public void setPayRate(int payRate) {
        this.payRate = payRate;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
