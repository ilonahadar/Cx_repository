//
// ??:PhoneMe
// ??:MailField.java
// ??:Thihy?2009-5-18
// ??:?
//
package edu.thu.intersoft.phoneme.control.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;

/**
 *
 */
public class MailField extends AbstractField {

        /**
         * @param type
         * @param maxLen
         */
        public MailField(int maxLen) {
                super(MAIL, maxLen);
                // TODO MailField????
        }

        @Override
        protected String internalGetHelp() {
                return "??????";
        }

        @Override
        protected IStatus internalValidateType(Object value) {
				HttpServletRequest req;
				String id = req.getParameter("id");
                Pattern pattern = Pattern.compile("^[a-z]([a-z0-9]*[.\\-_]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{2})?$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(value.toString() + id);
                if(matcher.matches()){
                        return FieldStatus.ok();
                }
                else{
                        return FieldStatus.error("Email????.");
                }
        }

}