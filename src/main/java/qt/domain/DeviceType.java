package qt.domain;
// Generated 2017-5-9 21:00:25 by Hibernate Tools 3.2.2.GA


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DeviceType generated by hbm2java
 */
@Entity
@Table(name="device_type")
public class DeviceType  implements java.io.Serializable {


     private Integer id;
     private String type;
     private String pic;

    public DeviceType() {
    }

	
    public DeviceType(Integer id) {
        this.id = id;
    }
    public DeviceType(Integer id, String type, String pic) {
       this.id = id;
       this.type = type;
       this.pic = pic;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Column(name="type", length=45)
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Column(name="pic", length=256)
    public String getPic() {
        return this.pic;
    }
    
    public void setPic(String pic) {
        this.pic = pic;
    }




}


