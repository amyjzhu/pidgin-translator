package test.programs.conference;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import test.programs.conference.User.Role;

public class ClientTest {

    public static void main(String[] args) {
        RequestHandler handler = new RequestHandler();
        
        Set<Role> authorRoles = new HashSet<Role>();
        authorRoles.add(Role.AUTHOR);
        
        Set<Role> reviewerRoles = new HashSet<Role>();
        reviewerRoles.add(Role.REVIEWER);
        
        Set<Role> pcRoles = new HashSet<Role>();
        pcRoles.add(Role.PCMEMBER);
        pcRoles.add(Role.PCCHAIR);
        
        Deadlines.setDeadlines(false, false, false, false);
        
        User author =  new User("me", authorRoles, Collections.<Paper> emptySet());
        User reviewer =  new User("jerk", reviewerRoles, Collections.<Paper> emptySet());
        User pc =  new User("famous", pcRoles, Collections.<Paper> emptySet());

        Paper p = new Paper(Collections.singleton("me"), "awesome new ideas");
        Review r =  new Review("it sucks", reviewer);
        
        while(true) {
            handler.addPaper(author, p);
            handler.addReviewer(pc, p, reviewer);
            String text = handler.getPaperText(reviewer, p);
            handler.addReview(reviewer, r, text);
            handler.getAuthors(pc, p);
            handler.getReviews(author, p);
            handler.acceptPaper(pc, p);
            handler.rejectPaper(pc, p);
            handler.getPaperStatus(author, p);
            handler.markNotificationDeadline(pc, true);
            handler.markCameraReadyDeadline(pc, true);
            handler.markReviewDeadline(pc, true);
            handler.markSubmissionDeadline(pc, true);
            handler.addConflict(author, p);
        }
    }

}
