package test.programs.conference;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class PaperDatabase {
    
    private final Set<Paper> papers = new HashSet<Paper>();
    private final Map<Paper, Boolean> accepted = new HashMap<Paper, Boolean>();
    private final Map<Paper, Set<Review>> reviews = new HashMap<Paper, Set<Review>>();
    private final Map<Paper, Set<User>> reviewers = new HashMap<Paper, Set<User>>();    
    private final Map<String, Paper> textToPaper = new HashMap<String, Paper>();
    
    public void acceptPaper(Paper p) {
         accepted.put(p, true);
    }
    
    public void rejectPaper(Paper p) {
        accepted.put(p, false);
    }
    
    public boolean addPaper(Paper p) {
        if (papers.contains(p)) {
            return false;
        }
        reviews.put(p, new HashSet<Review>());
        reviewers.put(p, new HashSet<User>());
        papers.add(p);
        return true;
    }
    
    /**
     * True if this is a new review, false if it is an updated one
     */
    public boolean addReview(Paper p, Review r) {
        Set<Review> revs = reviews.get(p);
        return revs.add(r);
    }
        
    public void addReviewer(Paper p, User reviewer) {
        Set<User> revs = reviewers.get(p);
        revs.add(reviewer);
    }
    
    public Set<String> getReviews(Paper p) {
        Set<String> reviewText = new LinkedHashSet<String>();
        for (Review r : reviews.get(p)) {
            reviewText.add(r.getReview());
        }
        return reviewText;
    }

    public boolean isReviewerOf(User current, Paper p) {
        Set<User> revs = reviewers.get(p);
        return revs.contains(current);
    }
    
    public Paper getPaper(String paperText) {
        return textToPaper.get(paperText);
    }

    public Boolean isAccepted(Paper p) {
        return accepted.get(p);
    }
}
