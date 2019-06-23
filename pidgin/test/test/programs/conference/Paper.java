package test.programs.conference;

import java.util.Set;

public class Paper {

    private final Set<String> authors;
    private final String text;

    public Paper(Set<String> authors, String text) {
        this.authors = authors;
        this.text = text;
    }

    public boolean isAuthorOf(User u) {
        return authors.contains(u.name());
    }
    
    public Set<String> getAuthors() {
        return authors;
    }
    
    public String getText() {
        return text;
    }
}
