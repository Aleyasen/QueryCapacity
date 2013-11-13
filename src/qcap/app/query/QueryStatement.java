/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.query;

/**
 *
 * @author aleyase2-admin
 */
public class QueryStatement {

    Integer id;
    String attribute;
    String value;
    Double weight;

    public QueryStatement() {
        weight = 1.0d;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "QueryStatement{" + "id=" + id + ", attribute=" + attribute + ", value=" + value + ", weight=" + weight + '}';
    }
}
