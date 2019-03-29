package edu.miracosta.cs134.model;

import java.util.Objects;

/**
 * Represents a Student for the purposes of the Superheroe quiz, including the fileName, name,
 * superPower, and oneThing.
 *
 * @author Jay Montoya
 * @version 1.0
 */
public class Student {

    private String mFileName;
    private String mName;
    private String mSuperPower;
    private String mOneThing;

    /**
     * Instatiates a new <code>Student</code> given its name and region.
     * @param fileName
     * @param name
     * @param superPower
     * @param oneThing
     */
    public Student(String fileName, String name, String superPower, String oneThing) {
        mFileName = fileName;
        mName = name;
        mSuperPower = superPower;
        mOneThing = oneThing;
    }

    public String getFileName() {
        return mFileName;
    }

    public String getName() {
        return mName;
    }

    public String getSuperPower() {
        return mSuperPower;
    }

    public String getOneThing() {
        return mOneThing;
    }

    @Override
    public String toString() {
        return "Student{" +
                "mFileName='" + mFileName + '\'' +
                ", mName='" + mName + '\'' +
                ", mSuperPower='" + mSuperPower + '\'' +
                ", mOneThing='" + mOneThing + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(mFileName, student.mFileName) &&
                Objects.equals(mName, student.mName) &&
                Objects.equals(mSuperPower, student.mSuperPower) &&
                Objects.equals(mOneThing, student.mOneThing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mFileName, mName, mSuperPower, mOneThing);
    }
}
