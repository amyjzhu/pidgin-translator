package test.programs.conference;

import java.util.Set;

public class RequestHandler {

    private PaperDatabase db = new PaperDatabase();

    public void addPaper(User current, Paper p) {
        if (current.isAuthor() && !Deadlines.submissionDeadlinePassed()) {
            db.addPaper(p);
        } else {
            System.err.println("No, you can't do that.");
        }
    }

    public void addReview(User current, Review r, String paperText) {
        Paper p = db.getPaper(paperText);
        if (db.isReviewerOf(current, p)) {
            db.addReview(p, r);
        } else {
            System.err.println("No, you can't do that.");
        }
    }

    public void acceptPaper(User current, Paper p) {
        if (current.isPCChair()) {
            db.acceptPaper(p);
        } else {
            System.err.println("No, you can't do that.");
        }
    }

    public void rejectPaper(User current, Paper p) {
        if (current.isPCChair()) {
            db.rejectPaper(p);
        } else {
            System.err.println("No, you can't do that.");
        }
    }

    public void addReviewer(User current, Paper p, User reviewer) {
        if (current.isPCChair()) {
            db.addReviewer(p, reviewer);
        } else {
            System.err.println("No, you can't do that.");
        }
    }

    public Set<String> getReviews(User current, Paper p) {
        if (    db.isReviewerOf(current, p) ||
                (p.isAuthorOf(current) && Deadlines.notificationDeadlinePassed()) ||
                (current.isPCMember() && !current.hasConflict(p)) ||
                current.isAdmin()) {
            return db.getReviews(p);
        } else {
            System.err.println("No, you can't do that.");
        }
        return null;
    }

    public Set<String> getAuthors(User current, Paper p) {
        if (p.isAuthorOf(current) || current.isPCChair() || current.isAdmin()) {
            return p.getAuthors();
        } else {
            System.err.println("No, you can't do that.");
        }
        return null;
    }

    public String getPaperText(User current, Paper p) {
        if (    p.isAuthorOf(current) ||
                (current.isPCMember() && !current.hasConflict(p)) ||
                current.isAdmin()) {
            return p.getText();
        }
        return null;
    }

    public Boolean getPaperStatus(User current, Paper p) {
        if ((p.isAuthorOf(current) && Deadlines.notificationDeadlinePassed()) ||
                current.isPCChair() || (current.isPCMember() && !current.hasConflict(p))) {
            return db.isAccepted(p);
        } else {
            System.err.println("No, you can't do that.");
        }
        return null;
    }

    public void markNotificationDeadline(User current, Boolean hasPassed) {
        if (!Deadlines.notificationDeadlinePassed() &&
                (current.isAdmin() || current.isPCChair())) {
            Deadlines.setNotification(hasPassed);
        } else {
            System.err.println("No, you can't do that.");
        }
    }

    public void markSubmissionDeadline(User current,  Boolean hasPassed) {
        if (!Deadlines.submissionDeadlinePassed() &&
                (current.isAdmin() || current.isPCChair())) {
            Deadlines.setSubmission(hasPassed);
        } else {
            System.err.println("No, you can't do that.");
        }
    }

    public void markReviewDeadline(User current,  Boolean hasPassed) {
        if (!Deadlines.reviewDeadlinePassed() &&
                (current.isAdmin() || current.isPCChair())) {
            Deadlines.setReview(hasPassed);
        } else {
            System.err.println("No, you can't do that.");
        }
    }
    
    public void markCameraReadyDeadline(User current,  Boolean hasPassed) {
        if (!Deadlines.cameraReadyDeadlinePassed() &&
                (current.isAdmin() || current.isPCChair())) {
            Deadlines.setCameraReady(hasPassed);
        } else {
            System.err.println("No, you can't do that.");
        }
    }
    
    public void addConflict(User current, Paper conflict) {
        current.addConflict(conflict);
    }
}
