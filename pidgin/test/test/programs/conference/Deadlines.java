package test.programs.conference;

public class Deadlines {

    private static boolean submission;
    private static boolean review;
    private static boolean cameraReady;
    private static boolean notification;

    public static boolean submissionDeadlinePassed() {
        return submission;
    }

    public static boolean reviewDeadlinePassed() {
        return review;
    }

    public static boolean notificationDeadlinePassed() {
        return notification;
    }

    public static boolean cameraReadyDeadlinePassed() {
        return cameraReady;
    }

    public static void setDeadlines(boolean submission, boolean review, boolean cameraReady, boolean notification) {
        Deadlines.submission = submission;
        Deadlines.cameraReady = cameraReady;
        Deadlines.review = review;
        Deadlines.notification = notification;
    }

    public static void setCameraReady(boolean cameraReady) {
        Deadlines.cameraReady = cameraReady;
    }

    public static void setReview(boolean review) {
        Deadlines.review = review;
    }

    public static void setNotification(boolean notification) {
        Deadlines.notification = notification;
    }

    public static void setSubmission(boolean submission) {
        Deadlines.submission = submission;
    }
}
