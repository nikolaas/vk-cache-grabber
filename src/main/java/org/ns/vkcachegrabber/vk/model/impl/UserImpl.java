package org.ns.vkcachegrabber.vk.model.impl;

import java.util.Objects;
import org.ns.vkcachegrabber.vk.model.User;

/**
 *
 * @author stupak
 */
public class UserImpl implements User {

    private String id;
    private String firstName;
    private String lastName;
    
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserImpl other = (UserImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserImpl{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + '}';
    }
    
}
