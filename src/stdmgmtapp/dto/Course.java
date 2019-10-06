/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stdmgmtapp.dto;

/**
 *
 * @author hp
 */
public class Course {
    Integer id;
    String label;
    Integer hours_number;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getHours_number() {
        return hours_number;
    }

    public void setHours_number(Integer hours_number) {
        this.hours_number = hours_number;
    }
    
}
