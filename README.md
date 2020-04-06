# AndroidContactApplication
Contact Application for Android Phones 

MainActivity
- Use of listview to display all contact list stored in database.
- Users can type the name in the search bar to search for contact. Listview will change each time user types in a letter and display the contact name based on the text pattern.
- Users can also add contacts by clicking on the add image button.

DisplayContacts Activity
- When user clicks on a name in the listview of MainActivity, DisplayContacts will be displayed to show contact information such as:
- Name
- Phone number
- Phone type (Mobile, home, etc.)
- User has the option to edit/delete the contact.

EditContacts Activity
- If contact information changes, user can edit their contacts.
- Contact name and number will be displayed by default.
- Spinner position for phone type will also be set to the user's default phone type.
- Once information is edited and saved, EditContact activity will close and display MainActivity.
- Activity is also used to AddContact.
