package test.programs.conference;

public class Review {
    private final String review;
    
    private final User reviewer;
    
    public Review(String review, User reviewer) {
        this.review = review;
        this.reviewer = reviewer;
    }
    
    public String getReview() {
        return review;
    }
    
    public User getReviewer() {
        return reviewer;
    }
}
