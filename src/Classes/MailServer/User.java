package Classes.MailServer;

import Classes.DataStructures.DoublyLinkedList;
import Classes.Misc.Birthday;
import Classes.Misc.Utils;
import Interfaces.MailServer.IContact;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;

public class User implements IContact {


    private final String address;
    private final DoublyLinkedList contacts = new DoublyLinkedList();
    private String encryptedPassword;
    private String name, filePath;
    private String gender;
    private Birthday birthday;


    public User(String address, String encryptedpassword) {
        this.address = address;
        this.encryptedPassword = encryptedpassword;
    }

    public User(String address) {
        this.address = address;
    }

    /**
     * adds to list and to csv file
     *
     * @param contact to be added
     * @throws IOException file not found
     */
    public void addContact(Contact contact) {
        contact.setIndex(contacts.size());
        contact.setOwner(this);
        contacts.add(contact);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFilePath() + "\\contacts.csv", true));
            writer.write(contact.getName() + "," + contact.getAddressesString());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            Utils.fileNotFound();
        }
    }

    public DoublyLinkedList sortContactsByIndex() {
        Sort sort = new Sort(SortAttribute.contactIndex);
        Comparator c = sort.sortAttribute();
        contacts.Qsort(c);
        return contacts;
    }

    public DoublyLinkedList sortContactsByName() {
        Sort sort = new Sort(SortAttribute.contactName);
        Comparator c = sort.sortAttribute();
        contacts.Qsort(c);
        return contacts;
    }

    public DoublyLinkedList getContactByName(String Name) {
        Name = Name.toLowerCase();
        DoublyLinkedList res = new DoublyLinkedList();
        Iterator<Contact> iter = contacts.iterator(true);
        for (int i = 0; iter.hasNext(); i++) {
            Contact c = iter.next();
            if (c.getName().toLowerCase().startsWith(Name)) res.add(c);
        }
        return res;
    }

    public DoublyLinkedList getContactByAddress(String address) {
        address = address.toLowerCase();
        DoublyLinkedList res = new DoublyLinkedList();
        Iterator<Contact> iter1 = contacts.iterator(true);
        while (iter1.hasNext()) {
            Contact c = iter1.next();
            Iterator<String> iter2 = c.getAddresses().iterator(true);
            while (iter2.hasNext()) {
                String s = iter2.next();
                if (s.startsWith(address)) res.add(c);
            }
        }
        return res;
    }

    protected boolean delContact(@NotNull Contact contact) {
        Iterator<Contact> iter = contacts.iterator(true);
        int index;
        for (int i = 0; iter.hasNext(); i++) {
            Contact con = iter.next();
            if (contact.equals(con)) {
                contacts.remove(i);
                index = con.getIndex();
                try {
                    exportContacts();
                } catch (IOException e) {
                    Utils.fileNotFound();
                }
                Iterator<Contact> iter2 = contacts.iterator(true);
                //minus 1 from indexes
                while (iter2.hasNext()) {
                    Contact c = iter2.next();
                    if (c.getIndex() > index) {
                        c.setIndex(c.getIndex() - 1);
                    }
                }
                return true;
            }
        }
        return false;
    }

    protected void exportContacts() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFilePath() + "\\contacts.csv", false));
        if (contacts.isEmpty()) return;
        Iterator<Contact> iter = contacts.iterator(true);
        while (iter.hasNext()) {
            Contact contact = iter.next();
            writer.write(contact.getName() + "," + contact.getAddressesString());
            writer.newLine();
        }
        writer.close();
    }

    public String getFilePath() {
        return filePath;
    }

    public String getAddress() {
        return address;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setBirthday(Birthday bd) {
        this.birthday = bd;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DoublyLinkedList getContacts() {
        return contacts;
    }
}
