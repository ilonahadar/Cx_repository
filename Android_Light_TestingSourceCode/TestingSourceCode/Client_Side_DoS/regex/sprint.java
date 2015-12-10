/*
   ckleung 01/25/2001

   Class to provide sprintf functions.

*/

//
// Copyright(c) 2002, Chris Leung
//

package com.port80.util;


import java.io.*;
import java.net.*;
import java.util.*;
//import java.security.*;
//import java.security.cert.*;
//import com.sun.net.ssl.*;
import org.apache.oro.text.perl.Perl5Util;

/** Formatted print like one in C.
 *
 *  . Addition date formats:
 *
Now(T0)=|185743|
Now(TT)=|18:57:43|
Now(TH)=|18:57|
Now(TM)=|57:43|
Now(YY)=|2002|
Now(MM)=|3|
Now(WW)=|Fri|
Now(D0)=|20020308|
Now(DD)=|2002-03-08|
Now(DM)=|3-8|
Now(Dm)=|3-8|
Now(Ds)=|3|
Now(DH)=|18:57 3-8|
Now(Dh)=|3-8 18:57|
Now(DY)=|2002-3|
Now(Dy)=|2002-3|
Now(DT)=|2002-03-08 18:57:43|
Now(U0)=|03082002|
Now(UD)=|03/08/2002|
Now(UM)=|3/8|
Now(Um)=|3/8|
Now(Us)=|3|
Now(UH)=|18:57 3/8|
Now(Uh)=|3/8 18:57|
Now(UY)=|3/2002|
Now(Uy)=|3/2002|
Now(UT)=|03/08/2002 18:57:43|
Now(D0 T0)=|20020308 185743|
*
*/

public class sprint {

    // Static fields ///////////////////////////////////////////////////////
    //

    static private String       hexTable        ="0123456789abcdef";

    static public final String  INTFORMATS      ="d|x|X";
    static public final String  FLOATFORMATS    ="f";
    static public final String  DOUBLEFORMATS   ="g";
    static public final String  STRINGFORMATS   ="s";
    static public final String  DATEFORMATS     ="T0|TT|TH|TM|YY|MM|WW|D0|DD|DM|Dm|Ds|DH|Dh|DY|Dy|DT|U0|UD|UM|Um|Us|UH|Uh|UY|Uy|UT";

    static public final String RE_DEC           ="/%([\\-])*([0-9]+)*l?d/";
    static public final String RE_HEX           ="/%([\\-])*([0-9]+)*([xX])/";
    static public final String RE_FLOAT         ="/%([\\-]+)*([0-9]+)*(\\.)*([0-9]+)*[fg]/";
    static public final String RE_STRING        ="/%([\\-])*([0-9]+)*s/";
    static public final String RE_DATE          ="/%([\\-])*([0-9]+)*("+DATEFORMATS+")/";

    public static boolean DEBUG=false;

    // Instance fields /////////////////////////////////////////////////////
    //

    static class Format {
        char type='?';
        String prefix=null;
        String[] groups=null;
        //
        Format(String prefix) {
            this.prefix=(prefix==null)? "": prefix;
        }
        public String toString() {
            return type+": "+prefix+": "+groups;
        }
    }

    private String formatString=null;
    private List formats=new ArrayList();
    private int argn=0;
    private StringBuffer ret=new StringBuffer("");

    // Static methods //////////////////////////////////////////////////////
    //

    static public synchronized sprint f(String fmt) {
        return new sprint().init(fmt);
    }

    private sprint() {}

    private sprint init(String format) {
        // FIXME: Doing the perl quick and dirty way here.
        // NOTE:
        // . Perl5Util.split put the separator (that match the pattern) into the result array too !
        // . If patthen match at the start and end, there is a null string at the start and end of the
        //   result array correspondingly.
        formatString=format;
        List a=msg.split("/(%[\\-]*[0-9\\.]*l?(?:"
                         +INTFORMATS
                         +"|"+FLOATFORMATS
                         +"|"+DOUBLEFORMATS
                         +"|"+STRINGFORMATS
                         +"|"+DATEFORMATS
                         +"))/",
                         format);
        for(int i=0,max=a.size(); i<max;) {
            Format f=new Format((String)a.get(i++));
            if(i<max) {
                String fmt=(String)a.get(i++);
                if((f.groups=msg.groups(RE_DEC,fmt))!=null) {
                    f.type='d';
                } else if((f.groups=msg.groups(RE_HEX,fmt))!=null) {
                    f.type='x';
                } else if((f.groups=msg.groups(RE_STRING,fmt))!=null) {
                    f.type='s';
                } else if((f.groups=msg.groups(RE_FLOAT,fmt))!=null) {
                    f.type='f';
                } else if((f.groups=msg.groups(RE_DATE,fmt))!=null) {
                    f.type='t';
                } else msg.warn("sprint.f(): invalid format: "+fmt);
            }
            formats.add(f);
        }
        if(DEBUG) for(int i=0; i < formats.size(); i++ ) {
            msg.println("DEBUG: sprint.f(): field[" + i + "]= " + (String)formats.get(i));
        }
        return this;
    }

    // Instance methods ////////////////////////////////////////////////////
    //

    public sprint a(int value) {
        final String FUNC="sprint.a(int)";
        if(argn>=formats.size()) {
            msg.println(FUNC+": more argument than format spec."
                        +": argn="+argn
                        +": formatString="+formatString
                        );
            return null;
        }
        Format f=(Format)formats.get(argn);
        ret.append(f.prefix);
        switch(f.type) {
        case 'd': printDec(f,(long)value); break;
        case 'x': printHex(f,(long)value); break;
        case 'f': printDouble(f,(double)value); break;
        case 's': printString(f,""+value); break;
        default:
            msg.err(FUNC+": Invalid value for format"
                    +": type="+f.type
                    +": argn="+argn
                    +": formatString="+formatString
                    +": value="+value
                    );
            return null;
        }
        argn++;
        return this;
    }

    public sprint a(long value) {
        final String FUNC="sprint.a(long)";
        if(argn>=formats.size()) {
            msg.println(
                        FUNC+": more argument than format spec."
                        +": argn="+argn
                        +": formatString="+formatString
                        );
            return null;
        }
        Format f=(Format)formats.get(argn);
        ret.append(f.prefix);
        switch(f.type) {
        case 'd': printDec(f,value); break;
        case 'x': printHex(f,value); break;
        case 'f': printDouble(f,(double)value); break;
        case 's': printString(f,""+value); break;
        case 't': printDate(f,value); break;
        default:
            msg.err(FUNC+": Invalid value for format"
                    +": type="+f.type
                    +": argn="+argn
                    +": formatString="+formatString
                    +": value="+value
                    );
            return null;
        }
        argn++;
        return this;
    }

    public sprint a(float value) {
        final String FUNC="sprint.a(float)";
        if(argn>=formats.size()) {
            msg.println(FUNC+": more argument than format spec."
                        +": argn="+argn
                        +": formatString="+formatString
                        );
            return null;
        }
        Format f=(Format)formats.get(argn);
        ret.append(f.prefix);
        switch(f.type) {
        case 'd': printDec(f,(long)value); break;
        case 'x': printHex(f,(long)value); break;
        case 'f': printDouble(f,(double)value); break;
        case 's': printString(f,""+value); break;
        default:
            msg.err(FUNC+": Invalid value for format"
                    +": type="+f.type
                    +": argn="+argn
                    +": formatString="+formatString
                    +": value="+value
                    );
            return null;
        }
        argn++;
        return this;
    }

    public sprint a(double value) {
        final String FUNC="sprint.a(double)";
        if(argn>=formats.size()) {
            msg.println(FUNC+": more argument than format spec."
                        +": argn="+argn
                        +": formatString="+formatString
                        );

            return null;
        }
        Format f=(Format)formats.get(argn);
        ret.append(f.prefix);
        switch(f.type) {
        case 'd': printDec(f,(long)value); break;
        case 'x': printHex(f,(long)value); break;
        case 'f': printDouble(f,value); break;
        case 's': printString(f,""+value); break;
        default:
            msg.err(FUNC+": Invalid value for format"
                    +": type="+f.type
                    +": argn="+argn
                    +": formatString="+formatString
                    +": value="+value
                    );
            return null;
        }
        argn++;
        return this;
    }

    public sprint a(String value) {
        final String FUNC="sprint.a(String)";
        if(argn>=formats.size()) {
            msg.println(FUNC+": more argument than format spec."
                        +": argn="+argn
                        +": formatString="+formatString
                        );
            return null;
        }
        Format f=(Format)formats.get(argn);
        ret.append(f.prefix);
        switch(f.type) {
        case 's': printString(f,value); break;
        default:
            msg.err(FUNC+": Invalid value for format"
                    +": type="+f.type
                    +": argn="+argn
                    +": formatString="+formatString
                    +": value="+value
                    );
            return null;
        }
        argn++;
        return this;
    }

    /** Print a date as formatted date/time string.
        %D=yyyy-mm-dd
        %U=mm/dd/yyyy
        %T=hh:mm:ss
     */
    public sprint a(Date value) {
        final String FUNC="sprint.a(Date)";
        if(argn>=formats.size()) {
            msg.println(FUNC+": more argument than format spec."
                        +": argn="+argn
                        +": formatString="+formatString
                        );
            return null;
        }
        Format f=(Format)formats.get(argn);
        ret.append(f.prefix);
        switch(f.type) {
        case 't': printDate(f,value); break;
        default:
            msg.err(FUNC+": Invalid value for format"
                    +": type="+f.type
                    +": argn="+argn
                    +": formatString="+formatString
                    +": value="+value
                    );
            return null;
        }
        argn++;
        return this;
    }

    public String end() {
        while(argn<formats.size()) {
            Format f=(Format)formats.get(argn++);
            ret.append(f.prefix);
        }
        return ret.toString();
    }

    // Helper methods //////////////////////////////////////////////////////
    //

    /** Print value in decimal format: /%([\\-])*([0-9]+)*[l]d/
     */
    private void printDec(Format f,long value) {
        boolean left=f.groups[0]!=null; // Left justify.
        boolean zero=false;             // Zero padded.
        int width=0;
        String wstr=f.groups[1];
        if(wstr!=null) {
            if(wstr.charAt(0)=='0') zero=true;
            try {
                width=Integer.parseInt(wstr);
            } catch(Exception e) {msg.err("sprint.printDec(): "+f,e);}
        }
        StringBuffer s=new StringBuffer(""+value);
        // Pad 0/space.
        padding(s,width,left,zero);
        ret.append(s);
    }

    /** Print value in hex format: /%([\\-])*([0-9]+)*([xX])/
     */
    private void printHex(Format f,long value) {
        boolean left=f.groups[0]!=null;
        boolean zero=false;
        int width=0;
        String wstr=f.groups[1];
        if(wstr!=null) {
            if(wstr.charAt(0)=='0') zero=true;
            try {
                width=Integer.parseInt(wstr);
            } catch(Exception e){ msg.err("sprint.printHex(): "+f,e);}
        }
        boolean upper=f.groups[2].charAt(0)=='X';
        StringBuffer s=new StringBuffer(toHexString(value,upper,width));
        padding(s,width,left,zero);
        ret.append(s);
    }

    /** Print value in floating point format: /%([\\-]+)*([0-9]+)*(\\.)*([0-9]+)*[fg]/
     */
    private void printDouble(Format f,double value) {
        boolean left=f.groups[0]!=null;
        boolean zero=false;
        int width=0;
        int decimals=0;
        try {
            String wstr=f.groups[1];
            if(wstr!=null) {
                if(wstr.charAt(0)=='0') zero=true;
                width=Integer.parseInt(wstr);
            }
            wstr=f.groups[3];
            if(wstr!=null) {
                decimals=Integer.parseInt(wstr);
            }
        } catch(Exception e) { msg.err("sprint.printDouble(): "+f,e);}
        StringBuffer s=new StringBuffer(toDoubleString(value,width,decimals));
        padding(s,width,left,zero);
        ret.append(s);
    }

    /** Print value in string format: /%([\\-])*([0-9]+)*s/
     */
    private void printString(Format f,String value) {
        boolean left=f.groups[0]!=null;
        int width=0;
        String wstr=f.groups[1];
        if(wstr!=null) {
            try {
                width=Integer.parseInt(wstr);
            } catch(Exception e) { msg.err("sprint.printString(): "+f,e);}
        }
        StringBuffer s=new StringBuffer(value);
        padding(s,width,left,false);
        ret.append(s);
    }

    /** Print value in date format: "/%([\\-])*([0-9]+)*("+DATEFORMATS+")/"
     */
    private void printDate(Format f,long value) {
        printDate(f,new Date(value));
    }

    private void printDate(Format f,Date date) {
        boolean left=f.groups[0]!=null;
        int width=0;
        String wstr=f.groups[1];
        if(wstr!=null) {
            try {
                width=Integer.parseInt(wstr);
            } catch(Exception e) { msg.err("sprint.printDate(): "+f,e);}
        }
        StringBuffer s=new StringBuffer();
        String fmt=f.groups[2];
        if(fmt.equals("T0")) s.append(DateTime.getInstance(date).getNumericTimeString());
        else if(fmt.equals("TT")) s.append(DateTime.getInstance(date).getTimeString());
        else if(fmt.equals("TH")) s.append(DateTime.getInstance(date).getHourMinuteString());
        else if(fmt.equals("TM")) s.append(DateTime.getInstance(date).getMinuteSecondString());
        else if(fmt.equals("YY")) s.append(DateTime.getInstance(date).getYearString());
        else if(fmt.equals("MM")) s.append(DateTime.getInstance(date).getMonthString());
        else if(fmt.equals("WW")) s.append(DateTime.getInstance(date).getWeekdayString());
        else if(fmt.equals("D0")) s.append(DateTime.getInstance(date).getNumericDateString());
        else if(fmt.equals("DD")) s.append(DateTime.getInstance(date).getDateString());
        else if(fmt.equals("DM")) s.append(DateTime.getInstance(date).getMonthDayString());
        else if(fmt.equals("Dm")) s.append(DateTime.getInstance(date).getStartOfMonthString());
        else if(fmt.equals("Ds")) s.append(DateTime.getInstance(date).getStartOfYearStartOfMonthString());
        else if(fmt.equals("DH")) s.append(DateTime.getInstance(date).getHourMinuteMonthDayString());
        else if(fmt.equals("Dh")) s.append(DateTime.getInstance(date).getMonthDayHourMinuteString());
        else if(fmt.equals("DY")) s.append(DateTime.getInstance(date).getYearMonthString());
        else if(fmt.equals("Dy")) s.append(DateTime.getInstance(date).getYearStartOfMonthString());
        else if(fmt.equals("DT")) s.append(DateTime.getInstance(date).getDateTimeString());
        else if(fmt.equals("U0")) s.append(DateTime.getInstance(date,"US").getNumericDateString());
        else if(fmt.equals("UD")) s.append(DateTime.getInstance(date,"US").getDateString());
        else if(fmt.equals("UM")) s.append(DateTime.getInstance(date,"US").getMonthDayString());
        else if(fmt.equals("Um")) s.append(DateTime.getInstance(date,"US").getStartOfMonthString());
        else if(fmt.equals("Us")) s.append(DateTime.getInstance(date,"US").getStartOfYearStartOfMonthString());
        else if(fmt.equals("UH")) s.append(DateTime.getInstance(date,"US").getHourMinuteMonthDayString());
        else if(fmt.equals("Uh")) s.append(DateTime.getInstance(date,"US").getMonthDayHourMinuteString());
        else if(fmt.equals("UY")) s.append(DateTime.getInstance(date,"US").getYearMonthString());
        else if(fmt.equals("Uy")) s.append(DateTime.getInstance(date,"US").getYearStartOfMonthString());
        else if(fmt.equals("UT")) s.append(DateTime.getInstance(date,"US").getDateTimeString());
        padding(s,width,left,false);
        ret.append(s);
    }


    ////////////////////////////////////////////////////////////////////////

    private StringBuffer padding( StringBuffer s, int width, boolean leftJustify, boolean zeroPadded ) {
        width-=s.length();
        for( ; width > 0; width-- ) {
            if( zeroPadded ) {
                s.insert(0,'0');
            } else if( leftJustify ) {
                s.append(' ');
            } else {
                s.insert(0,' ');
            }
        }
        return s;
    }

    private String toHexString( int value, boolean uppercase, int width ) {
        StringBuffer ret=new StringBuffer("");
        for( int i=0; i < width; i++ ) {
            int v=value & 0xf;
            ret.insert(0,hexTable.charAt(v));
            value >>>= 4;
        }
        for( int i=0; i < width-1; i++ ) {
            if( ret.charAt(0)=='0' ) ret.deleteCharAt(0);
            else break;
        }
        return (uppercase) ? ret.toString().toUpperCase() : ret.toString();
    }

    private String toHexString( long value, boolean uppercase, int width ) {
        StringBuffer ret=new StringBuffer("");
        for( int i=0; i < width; i++ ) {
            int v=(int)value & 0xf;
            ret.insert(0,hexTable.charAt(v));
            value >>>= 4;
        }
        for( int i=0; i < width-1; i++ ) {
            if( ret.charAt(0)=='0' ) ret.deleteCharAt(0);
            else break;
        }
        return (uppercase) ? ret.toString().toUpperCase() : ret.toString();
    }

    private String toFloatString( float value, int width, int decimal ) {
        return toDoubleString((double)value,width,decimal);
    }

    private String toDoubleString( double value, int width, int decimal ) {
        if(DEBUG) msg.debug("sprint.toDoubleString(): " + value + ": " + width + ": " + decimal);
        StringBuffer ret=new StringBuffer();
        String s=""+(long)value;
        for(int i=0;i<s.length(); i++) {
            char c=s.charAt(i);
            ret.append(c);
            //if(c=='.') {i++; break;}
            if(DEBUG) msg.debug("sprint.toDoubleString(): 1: " + ret.toString());
        }
        if(decimal>0) {
            ret.append('.');
            if(value<0) value=-value;
            double f=value-(long)value;
            for(int i=decimal; i>0; --i) {
                f=f*10;
                int d=(int)f;
                f-=d;
                ret.append(""+d);
                if(DEBUG) msg.debug("sprint.toDoubleString(): 2: " + ret.toString());
            }
        }
        if(DEBUG) msg.debug("sprint.toDoubleString(): 3: " + ret.toString());
        return ret.toString();
    }

    ////////////////////////////////////////////////////////////////////////

    public static void main( String[] args ) throws Exception {
        // Test routines for the class.
        List result;
        msg.println("### regex tests:");
        result=msg.split("/abc/","111abc222abc333");
        msg.print("result 1=");
        for( int i=0; i < result.size(); i++ ) msg.print((String)result.get(i) + " ");
        msg.println("");
        result=msg.split("/a(b)(c)/","111abc222abc333");
        msg.print("result 2=");
        for( int i=0; i < result.size(); i++ ) msg.print((String)result.get(i) + " ");
        msg.println("");
        result=msg.split("/(abc)/","111abc222abc333");
        msg.print("result 3=");
        for( int i=0; i < result.size(); i++ ) msg.print((String)result.get(i) + " ");
        msg.println("");
        //
        msg.println("### Number format tests:");
        msg.println(sprint.f("%.2f").a(123.2342384028e1).end());
        msg.println(sprint.f("%8.2f").a(123.2342384028e10).end());
        msg.println(sprint.f("%8.4f").a(0.01232342384028).end());
        msg.println(sprint.f("%8.2f").a(0.01232342384028).end());
        msg.println(sprint.f("%8.0f").a(0.01232342384028).end());
        msg.println(sprint.f("%-8f").a(0.01232342384028).end());
        //
        msg.println("### Date format tests:");
        Date now=new Date();
        long timenow=now.getTime();
        msg.println("");
        msg.println( sprint.f("|%02x,%06x|").a(0xa).a(0x1234).end());
        msg.println( sprint.f("|%-10d,%10d|").a(123).a(456).end());
        msg.println( sprint.f("|%-10d,%10d|").a(-123).a(+456).end());
        msg.println( sprint.f("|%-2d,%2d|").a(123).a(456).end());
        msg.println( sprint.f("|%-s,%s|").a("string1").a("string2").end());
        msg.println( sprint.f("|ssss|%-10s,%10s|xxxx|").a("string1").a("string2").end());
        msg.println( sprint.f("Now(T0)=|%T0|").a(now).end());
        msg.println( sprint.f("Now(TT)=|%TT|").a(now).end());
        msg.println( sprint.f("Now(TH)=|%TH|").a(now).end());
        msg.println( sprint.f("Now(TM)=|%TM|").a(now).end());
        msg.println( sprint.f("Now(YY)=|%YY|").a(now).end());
        msg.println( sprint.f("Now(MM)=|%MM|").a(now).end());
        msg.println( sprint.f("Now(WW)=|%WW|").a(now).end());
        msg.println( sprint.f("Now(D0)=|%D0|").a(now).end());
        msg.println( sprint.f("Now(DD)=|%DD|").a(now).end());
        msg.println( sprint.f("Now(DM)=|%DM|").a(now).end());
        msg.println( sprint.f("Now(Dm)=|%Dm|").a(now).end());
        msg.println( sprint.f("Now(Ds)=|%Ds|").a(now).end());
        msg.println( sprint.f("Now(DH)=|%DH|").a(now).end());
        msg.println( sprint.f("Now(Dh)=|%Dh|").a(now).end());
        msg.println( sprint.f("Now(DY)=|%DY|").a(now).end());
        msg.println( sprint.f("Now(Dy)=|%Dy|").a(now).end());
        msg.println( sprint.f("Now(DT)=|%DT|").a(now).end());
        msg.println( sprint.f("Now(U0)=|%U0|").a(now).end());
        msg.println( sprint.f("Now(UD)=|%UD|").a(now).end());
        msg.println( sprint.f("Now(UM)=|%UM|").a(now).end());
        msg.println( sprint.f("Now(Um)=|%Um|").a(now).end());
        msg.println( sprint.f("Now(Us)=|%Us|").a(now).end());
        msg.println( sprint.f("Now(UH)=|%UH|").a(now).end());
        msg.println( sprint.f("Now(Uh)=|%Uh|").a(now).end());
        msg.println( sprint.f("Now(UY)=|%UY|").a(now).end());
        msg.println( sprint.f("Now(Uy)=|%Uy|").a(now).end());
        msg.println( sprint.f("Now(UT)=|%UT|").a(now).end());
        msg.println( sprint.f("Now(D0T0)=|%D0 %T0|").a(timenow).a(timenow).end());
        try {
            msg.println( sprint.f("|%-2d,2d|").a(123).a(456).end()); // Intentional error here.
        } catch(Exception e) {
            msg.println("\n### Above is an intentional format error.");
        }
    }

    ////////////////////////////////////////////////////////////////////////

}