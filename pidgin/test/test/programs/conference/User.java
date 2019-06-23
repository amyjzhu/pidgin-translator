package test.programs.conference;

import java.util.Set;

public class User {
    
    private final String name;
    private final Set<Role> roles;
    private final Set<Paper> conflicts;
    
    public User(String name, Set<Role> roles, Set<Paper> conflicts) {
        this.name = name;
        this.roles = roles;
        this.conflicts = conflicts;
    }

    public enum Role {
        AUTHOR,
        REVIEWER,
        PCCHAIR,
        PCMEMBER,
        ADMIN;
    }
    
    public boolean isAuthor() {
        return roles.contains(Role.AUTHOR);
    }

    public boolean isPCChair(){
        return roles.contains(Role.PCCHAIR);
    }
    
    public boolean isPCMember(){
        return roles.contains(Role.PCMEMBER);
    }
    
    public boolean isAdmin(){
        return roles.contains(Role.ADMIN);
    }

    public String name() {
        return name;
    }

    public boolean hasConflict(Paper p) {
        Set<String> names = p.getAuthors();
        boolean conflict = false;
        for (String name : names) {
            conflict |= conflicts.contains(name);
        }
        return conflict;
    }

    public void addConflict(Paper conflict) {
        conflicts.add(conflict);
    }
}
