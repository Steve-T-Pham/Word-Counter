/**
 * A class to represent a hashtable with separate chaining used to handle collisions.
 * @author Steve Pham
 */
public class HashTable{
  
  //entry class to put into the hashtable
  private class Entry{
    //holds the key value
    private String key;
    //holds the data associated with the Entry
    private String etymology;
    //counts the amount of times the item has been inserted into the hashtable
    private int frequency = 1;
    
    //constructor
    private Entry(String key, String etymology){
      this.key = key;
      this.etymology = etymology;
    }
    
    //getter and setter methods
    private String getEtymology(){
      return etymology;
    }
    
    private String getKey(){
      return key;
    }
    
    private int getFrequency(){
      return frequency;
    }
    
    private void setFrequency(int frequency){
      this.frequency = frequency;
    }
    
    private void setKey(String key){
      this.key = key;
    }
    
    private void setEtymology(String etymology){
      this.etymology = etymology;
    }
  }
  
  //node class in order to be used for the LinkedList class
  private class LLNode{
    private Entry element;
    private LLNode next;
    
    //constructor
    public LLNode(Entry element, LLNode next) {
      this.element = element;
      this.next = next;
    }
    
    //getter and setter methods
    public Entry getElement() {
      return element;
    }
    
    public LLNode getNext() {
      return next;
    }
    
    public void setNext(LLNode next) {
      this.next = next;
    }
  }
  
  //linked list class to be used for separate chaining to avoid collisions
  private class LinkedList{
    private LLNode frontNode;
    
    public LLNode getFront(){
      return frontNode;
    }
    
    public void setFront(LLNode node){
      this.frontNode = node;
    }
    
    //adds an entry to the front of the list
    public void addToFront(Entry element){
      setFront(new LLNode(element, getFront()));
    }
    
    //determines if the linked list is empty or not
    public boolean isEmpty() {
      return (getFront() == null);
    }
    
    //calculates the length of the linkedlist
    public int length() {
      int lengthSoFar = 0;
      LLNode nodeptr = getFront();
      while (nodeptr != null){
        lengthSoFar++;
        nodeptr = nodeptr.getNext();
      }
      return lengthSoFar;
    }
    
    //adds an entry to the end of the linkedlist
    public void addToEnd(Entry element){
      if (isEmpty() == true)
        addToFront(element);
      else{
        LLNode nodeptr = getFront();
        while(nodeptr != null && nodeptr.getNext() != null)
          nodeptr = nodeptr.getNext();
        nodeptr.setNext(new LLNode(element, null));
      }
    }
    
    //deletes a node from a linkedlist
    public void deleteNode(String key){
      if (isEmpty() == true)
        throw new IllegalArgumentException("Empty List");
      else{
        LLNode nodeptr = getFront();
        int count = 0;
        if (nodeptr.getElement().getKey().equals(key))
          setFront(nodeptr.getNext());
        else{
          while (nodeptr != null && count < length() - 1 && nodeptr.getNext().getElement().getKey().equals(key) == false){
            count++;
            nodeptr = nodeptr.getNext();
          }
          if (count == length() - 1)
            throw new IllegalArgumentException("Node not found");
          else if (nodeptr.getNext().getNext() != null)
            nodeptr.setNext(nodeptr.getNext().getNext());
          else if (nodeptr.getNext() != null && nodeptr.getNext().getNext() == null)
            nodeptr.setNext(null);
        }
      }
    }
    
    //searches for a specific node in a linkedlist with the given key value
    public LLNode searchNode(String key){
      if (isEmpty() == true)
        return null;
      else {
        LLNode nodeptr = getFront();
        while (nodeptr != null && nodeptr.getElement().getKey().equals(key) == false && nodeptr.getNext() != null)
          nodeptr = nodeptr.getNext();
        if (nodeptr.getElement().getKey().equals(key))
          return nodeptr;
        else
          return null;
      }
    }
  }
  
/*+++++++++++++++++++++++++++++++++++Class Begins Here+++++++++++++++++++++++++++++++++++++++*/
  
  //represents the hashtable in an array form, holding linked lists
  private LinkedList[] table;
  
  //records the current size of the array/hashtable
  private int tableSize;
  
  //constructor
  public HashTable(int size){
    table = new LinkedList[size];
    for (int i = 0; i < table.length; i++){
      table[i] = new LinkedList();
    }
    tableSize = size;
  }
  
  //iterates through the hashtable and prints out every linkedlist element associated with every hashtable index
  public void printTable(){
    for (int i = 0; i < tableSize; i++){
      System.out.print(i + ". ");
      if (table[i].getFront() != null){
        LLNode nodeptr = table[i].getFront();
        while (nodeptr != null){
          System.out.print("{ " + nodeptr.getElement().getEtymology() + ", ");
          System.out.print(nodeptr.getElement().getFrequency() + " } ");
          nodeptr = nodeptr.getNext();
        }
      }
      System.out.print("\n");
    } 
  }
  
  //helper method to help find a specific Entry with a given key value, used for searching
  private int findKey(String key){
    int i = Math.abs(key.hashCode()) % tableSize; //given hash function
    LLNode nodeptr = table[i].getFront();
    while (nodeptr != null && nodeptr.getNext() != null && table[i].length() != 1 && nodeptr.getElement().getKey().equals(key) == false){
      nodeptr = nodeptr.getNext();
    }
    if (table[i].isEmpty() == true)
      return -1;
    if (nodeptr.getElement().getKey().equals(key))
      return i; //returns the index of the Entry 
    else
      return -1;
  }
  
  //method to return the associated Etymology or data associated with the key value given
  public String search(String key){
    int i = findKey(key); //calls upon helper method to determine if it is in the hashtable
    if (i == -1)
      return null;
    else{
      LLNode nodeptr = table[i].getFront();
      while (nodeptr != null && nodeptr.getNext() != null && table[i].length() != 1 && nodeptr.getElement().getKey().equals(key) == false)
        nodeptr = nodeptr.getNext();
      if (nodeptr.getElement().getKey().equals(key))
        return nodeptr.getElement().getEtymology();
      else
        return null;
    }
  }
  
  //removes an Entry from the hashtable
  public void remove(String key){
    int i = findKey(key); //determines if the Entry is in the hashtable
    if (i == -1)
      return;
    table[i].deleteNode(key); //deletes the Entry from whichever linked list or array index it was at
  }
  
  
  //inserts a new Entry into the hashtable
  public void insert(String key, String etymology){
    int i = Math.abs(key.hashCode()) % tableSize; //given hash function
    //Case 1 - if another Entry in the hashtable has the same key value, assuming there cannot be multiple etymologies assigned to the same key value, it overwrites it, and increments word count up by 1
    if (table[i].getFront() != null && table[i].searchNode(key) != null){
      LLNode nodeptr = table[i].getFront();
      while (nodeptr != null && nodeptr.getNext() != null && table[i].length() != 1 && nodeptr.getElement().getKey().equals(key) == false)
        nodeptr = nodeptr.getNext();
      if (nodeptr.getElement().getKey().equals(key)){
        nodeptr.getElement().setEtymology(etymology);
        nodeptr.getElement().setFrequency(nodeptr.getElement().getFrequency() + 1);
      }
    }
    //Case 2 - if there is not an entry found of the given key value, it will simply just add it at the end of the collission or linked list
    else if (table[i].getFront() != null && table[i].searchNode(key) == null){
      table[i].addToEnd(new Entry(key, etymology)); 
    }
    //Case 3 - if there is not a single node of a linked list at the given index after the hash function, will create a new node (add node to an empty list and initially gives it a frequency of 1)
    else{
      table[i].setFront(new LLNode(new Entry(key, etymology), null));
      table[i].getFront().getElement().setFrequency(1);
    }
  }
  
  /* rehashes the hashtable by creating a new array of a size that is double the old size and is also the next prime number,
   * then transfers over the old elements by replugging back the keys into the hashfunction with the new table size
   * this method avoids collissions once the table starts getting too full, such as words with too similar of hash function will end up being mapped to the same key
   */
  public void rehash(){
    int oldSize = tableSize;
    LinkedList[] oldTable = table;
    tableSize = nextPrime(2*oldSize); //calls upon helper function
    table = new LinkedList[tableSize];
    for (int i = 0; i < table.length; i++){
      table[i] = new LinkedList();
    }
    for(int j = 0; j < oldSize; j++){
     if (oldTable[j].getFront() == null)
       continue;
     else{
        LLNode nodeptr = oldTable[j].getFront();
        while (nodeptr != null){
          int oldFrequency = nodeptr.getElement().getFrequency();
          insert(nodeptr.getElement().getKey(), nodeptr.getElement().getEtymology());
          table[Math.abs(nodeptr.getElement().getKey().hashCode()) % tableSize].searchNode(nodeptr.getElement().getKey()).getElement().setFrequency(oldFrequency);
          nodeptr = nodeptr.getNext();
        }
      }
    }
  }
  
  //helper method to determine the next prime number for determining new tablesize
  public int nextPrime(int num) {
    num++;
    for (int i = 2; i < num; i++) {
      if (num % i == 0) {
        num++;
        i = 2;
      } 
      else {
        continue;
      }
    }
    return num;
  }
  
  
  //checks the load factor of the current hashtable to make sure that it does not go over 1, then rehashes if it does
  public void checkLoad(){
    int loadFactor = 0;
    for (int i = 0; i < table.length; i++){
      loadFactor = loadFactor + table[i].length();
    }
    if (loadFactor / tableSize > 1)
      rehash();
  }

                                
  //word count method that takes an incoming string, plugs every word into a hashtable and counts the number of occurrences
  public void wordCount(String s){
    for (int i = 0; i < s.split("\\P{Alpha}+").length; i++){
      if (search((s.split("\\P{Alpha}+")[i]).toLowerCase()) == null){ //converts all characters to lowercase then checks every word and places it in the hashtable
        insert((s.split("\\P{Alpha}+")[i].toLowerCase()), (s.split("\\P{Alpha}+")[i]).toLowerCase());
        checkLoad(); //calls whether to check if there is rehashing needed
      }
      else 
        table[Math.abs(s.split("\\P{Alpha}+")[i].toLowerCase().hashCode()) % tableSize].searchNode((s.split("\\P{Alpha}+")[i].toLowerCase())).getElement().setFrequency(table[Math.abs(s.split("\\P{Alpha}+")[i].toLowerCase().hashCode()) % tableSize].searchNode((s.split("\\P{Alpha}+")[i].toLowerCase())).getElement().getFrequency() + 1);
    }
    printTable();
  }
}
