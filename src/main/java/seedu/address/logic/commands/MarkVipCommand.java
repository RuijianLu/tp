package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class MarkVipCommand extends Command {

    public static final String COMMAND_WORD = "vip";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks or unmarks the person identified by the index "
            + "number used in the displayed person list as a VIP.\n"
            + "Parameters: INDEX (must be a positive integer) v/IS_VIP (must be \"true\" or \"false\")\n"
            + "Example: " + COMMAND_WORD + " 1 true";

    public static final String MESSAGE_VIP_PERSON_SUCCESS = "Person marked as a VIP: %1$s";
    public static final String MESSAGE_UNVIP_PERSON_SUCCESS = "VIP status removed from person: %1$s";
    public static final String MESSAGE_VIP_PERSON_OBSOLETE = "Person already a VIP: %1$s";
    public static final String MESSAGE_UNVIP_PERSON_OBSOLETE = "Person not a VIP: %1$s";
    private final String MESSAGE_SUCCESS;
    private final String MESSAGE_OBSOLETE;

    private final Index targetIndex;
    public final boolean newState;

    public MarkVipCommand(Index targetIndex, boolean newState) {
        this.targetIndex = targetIndex;
        this.newState = newState;
        if (newState) {
            MESSAGE_SUCCESS = MESSAGE_VIP_PERSON_SUCCESS;
            MESSAGE_OBSOLETE = MESSAGE_VIP_PERSON_OBSOLETE;
        } else {
            MESSAGE_SUCCESS = MESSAGE_UNVIP_PERSON_SUCCESS;
            MESSAGE_OBSOLETE = MESSAGE_UNVIP_PERSON_OBSOLETE;
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToMark = lastShownList.get(targetIndex.getZeroBased());
        if (personToMark.isVip() == newState) {
            return new CommandResult(String.format(MESSAGE_OBSOLETE, Messages.format(personToMark)));
        }
        Name name = personToMark.getName();
        Address address = personToMark.getAddress();
        Email email = personToMark.getEmail();
        Phone phone = personToMark.getPhone();
        Set<Tag> tags = personToMark.getTags();
        Person updatedPerson = new Person(name, phone, email, address, tags, newState);
        model.setPerson(personToMark, updatedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personToMark)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkVipCommand)) {
            return false;
        }

        MarkVipCommand otherMarkVipCommand = (MarkVipCommand) other;
        return targetIndex.equals(otherMarkVipCommand.targetIndex);
    }
}
