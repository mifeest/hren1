package commands;

import ticket.Ticket;

import java.io.Serializable;

public abstract class AbstractCommand implements Serializable, Cloneable  {
    protected CommandNames name;
    protected String specification;
    protected boolean mode;
    private String argument;
    private Ticket ticket;

    public CommandNames getName() {
        return name;
    }

    public void setName(CommandNames name) {
        this.name = name;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public void setInputData(String inputData) {
        this.argument = inputData;
    }

    public String getInputData() {
        return argument;
    }

    public String getSpecification() {
        return specification;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public abstract String use();


    @Override
    public String toString() {
        return this.name + " : " + this.specification;

    }

    @Override
    public AbstractCommand clone() {
        AbstractCommand clone = null;
        try {
            clone = (AbstractCommand) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;

    }
}
