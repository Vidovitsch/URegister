package domain;

import domain.TimeDiff;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-16T21:07:52")
@StaticMetamodel(Registratie.class)
public class Registratie_ { 

    public static volatile SingularAttribute<Registratie, String> date;
    public static volatile ListAttribute<Registratie, TimeDiff> times;
    public static volatile SingularAttribute<Registratie, Long> id;

}