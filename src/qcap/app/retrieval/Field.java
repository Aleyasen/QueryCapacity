/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.retrieval;

/**
 *
 * @author aleyase2-admin
 */
public class Field {

    protected String name;
    protected Long VocabCount;
    protected Long WordCount;

    public Field(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVocabCount() {
        return VocabCount;
    }

    public void setVocabCount(Long VocabCount) {
        this.VocabCount = VocabCount;
    }

    public Long getWordCount() {
        return WordCount;
    }

    public void setWordCount(Long WordCount) {
        this.WordCount = WordCount;
    }

}
