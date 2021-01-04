
public class student {
	private int id;
    private String name;
    private String gender;
    private String address;
    private String postcode;
    private float CS;
    private float math;
    private float eng;
    
    public student(int id, String name, String gender, String address, String postcode, float CS, float math, float eng){
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.CS = CS;
        this.math = math;
        this.eng = eng;
}
    
    public float getEng() {
        return eng;
    }

    /**
     * @param gender the gender to set
     */
    public void getEng(float eng) {
        this.eng = eng;
    }
    
    public float getMath() {
        return math;
    }

    /**
     * @param gender the gender to set
     */
    public void getMath(float math) {
        this.math = math;
    }
    
    public float getCS() {
        return CS;
    }

    /**
     * @param gender the gender to set
     */
    public void getCS(float CS) {
        this.CS = CS;
    }
    
    public String getPostcode() {
        return postcode;
    }

    /**
     * @param gender the gender to set
     */
    public void getPostcode(String postcode) {
        this.postcode = postcode;
    }
    
    public String getAddress() {
        return address;
    }

    /**
     * @param gender the gender to set
     */
    public void getAddress(String gender) {
        this.address = address;
    }
    
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the age
     */
    public int getid() {
        return id;
    }

    /**
     * @param age the age to set
     */
    public void setid(int id) {
        this.id = id;
    }
    public String toString(){
        return this.id + " " + this.name + " " + this.gender + " " + this.address + " " + this.postcode + " " + this.CS + " " + this.math + " " + this.eng ;
    }
}
