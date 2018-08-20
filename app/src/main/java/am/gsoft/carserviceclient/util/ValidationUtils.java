package am.gsoft.carserviceclient.util;

import am.gsoft.carserviceclient.data.database.entity.Oil;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;

public class ValidationUtils {

    private static final int FULL_NAME_MIN_LENGTH = 3;
    private static final int FULL_NAME_MAX_LENGTH = 50;
    private static final int PASSWORD_MIN_LENGTH = 8;
    private final static String NULL = "null";

    private Context context;

    public ValidationUtils(Context context) {
        this.context = context;
    }


    public boolean isKmDataValid(TextInputLayout serviceDoneKmTil,TextInputLayout nextServiceKmTil,TextInputLayout recomendedKmTil,TextInputLayout middleMonthKmTil,
            String serviceDoneKm, String nextServiceKm, String recomendedKm, String middleMonthKm,Oil currentOil){

        boolean isServiceDoneKmEntered = !TextUtils.isEmpty(serviceDoneKm.trim());
        boolean isNextServiceKmEntered = !TextUtils.isEmpty(nextServiceKm.trim());
        boolean isRecomendedKmEntered = !TextUtils.isEmpty(recomendedKm.trim());
        boolean isMiddleMonthKmEntered = !TextUtils.isEmpty(middleMonthKm.trim());

        if (isServiceDoneKmEntered && isNextServiceKmEntered && isRecomendedKmEntered) {
            boolean valid = true;
            if(!isServiceDoneKmValid(serviceDoneKm,currentOil)){
                valid = false;
//                emailTextInputLayout.setError(context.getString(R.string.auth_not_all_fields_entered));
                serviceDoneKmTil.setError("Curent Km must be more than the previous service km");
            }
            return valid;
        }else if (!isServiceDoneKmEntered && !isNextServiceKmEntered && !isRecomendedKmEntered) {
            serviceDoneKmTil.setError("Service Done Km Can Not Be Empty!");
            nextServiceKmTil.setError("Service Next Km Can Not Be Empty!");
            recomendedKmTil.setError("Recomended Km Can Not Be Empty!");
        } else {
            if (!isServiceDoneKmEntered) {
                serviceDoneKmTil.setError("Service Done Km Can Not Be Empty!");
            }
            if (!isNextServiceKmEntered) {
                nextServiceKmTil.setError("Service Next Km Can Not Be Empty!");
            }
            if (!isRecomendedKmEntered) {
                recomendedKmTil.setError("Recomended Km Can Not Be Empty!");
            }
        }

        return false;
    }

    private boolean isServiceDoneKmValid(String serviceDoneKm,Oil currentOil) {
        return Long.valueOf(serviceDoneKm)>currentOil.getServiceDoneKm();
    }

//    public boolean isLoginDataValid(TextInputLayout emailTextInputLayout,TextInputLayout passwordTextInputLayout, String email, String password) {
//
//        boolean isEmailEntered = !TextUtils.isEmpty(email.trim());
//        boolean isPasswordEntered = !TextUtils.isEmpty(password.trim());
//
//        if (isEmailEntered && isPasswordEntered) {
//            boolean valid = true;
//            if (!isEmailValid(email)) {
//                valid = false;
//                emailTextInputLayout.setError(context.getString(R.string.auth_email_field_is_incorrect));
//            }
//            if (password.length() < PASSWORD_MIN_LENGTH) {
//                valid = false;
//                passwordTextInputLayout.setError(context.getString(R.string.auth_password_field_is_too_short));
//            }
//            return valid;
//        } else if (!isEmailEntered && !isPasswordEntered) {
//            emailTextInputLayout.setError(context.getString(R.string.auth_not_all_fields_entered));
//            passwordTextInputLayout.setError(context.getString(R.string.auth_not_all_fields_entered));
//        } else {
//            if (!isEmailEntered) {
//                emailTextInputLayout.setError(context.getString(R.string.auth_email_field_not_entered));
//            }
//            if (!isPasswordEntered) {
//                passwordTextInputLayout.setError(context.getString(R.string.auth_password_field_not_entered));
//            }
//        }
//
//        return false;
//    }
//
//    public boolean isSignUpDataValid(TextInputLayout fullNameTextInputLayout,
//                                     TextInputLayout emailTextInputLayout, TextInputLayout passwordTextInputLayout, String fullName,
//                                     String email, String password) {
//        boolean isFullNameEntered = !TextUtils.isEmpty(fullName.trim());
//        boolean isEmailEntered = !TextUtils.isEmpty(email.trim());
//        boolean isPasswordEntered = !TextUtils.isEmpty(password.trim());
//
//        if (isFullNameEntered && isEmailEntered && isPasswordEntered) {
//            boolean valid = true;
//            if (fullName.length() < FULL_NAME_MIN_LENGTH) {
//                valid = false;
//                fullNameTextInputLayout.setError(context.getString(R.string.auth_full_name_field_is_too_short));
//            } else if (fullName.length() > FULL_NAME_MAX_LENGTH) {
//                valid = false;
//                fullNameTextInputLayout.setError(context.getString(R.string.auth_full_name_field_is_too_long));
//            }
//            if (!isEmailValid(email)) {
//                valid = false;
//                emailTextInputLayout.setError(context.getString(R.string.auth_email_field_is_incorrect));
//            }
//            if (password.length() < PASSWORD_MIN_LENGTH) {
//                valid = false;
//                passwordTextInputLayout.setError(context.getString(R.string.auth_password_field_is_too_short));
//            }
//            return valid;
//        } else if (!isFullNameEntered && !isEmailEntered && !isPasswordEntered) {
//            fullNameTextInputLayout.setError(context.getString(R.string.auth_not_all_fields_entered));
//            emailTextInputLayout.setError(context.getString(R.string.auth_not_all_fields_entered));
//            passwordTextInputLayout.setError(context.getString(R.string.auth_not_all_fields_entered));
//        } else {
//            if (!isFullNameEntered) {
//                fullNameTextInputLayout
//                        .setError(context.getString(R.string.auth_full_name_field_not_entered));
//            }
//            if (!isEmailEntered) {
//                emailTextInputLayout.setError(context.getString(R.string.auth_email_field_not_entered));
//            }
//            if (!isPasswordEntered) {
//                passwordTextInputLayout.setError(context.getString(R.string.auth_password_field_not_entered));
//            }
//        }
//
//        return false;
//    }
//
//    public boolean isChangePasswordDataValid(TextInputLayout oldPasswordTextInputLayout,
//                                             TextInputLayout newPasswordTextInputLayout, String oldPassword, String newPassword) {
//        boolean isOldPasswordEntered = !TextUtils.isEmpty(oldPassword.trim());
//        boolean isNewPasswordEntered = !TextUtils.isEmpty(newPassword.trim());
//
//        if (isOldPasswordEntered && isNewPasswordEntered) {
//            if (!qbUser.getPassword().equals(oldPassword)) {
//                oldPasswordTextInputLayout.setError(context.getString(R.string.change_password_old_password_wrong));
//            } else {
//                return true;
//            }
//        } else if (!isOldPasswordEntered && !isNewPasswordEntered) {
//            oldPasswordTextInputLayout.setError(context.getString(R.string.change_password_all_fields_not_entered));
//            newPasswordTextInputLayout.setError(context.getString(R.string.change_password_all_fields_not_entered));
//        } else {
//            if (!isOldPasswordEntered) {
//                oldPasswordTextInputLayout
//                        .setError(context.getString(R.string.change_password_old_password_not_entered));
//            }
//            if (!isNewPasswordEntered) {
//                newPasswordTextInputLayout
//                        .setError(context.getString(R.string.change_password_new_password_not_entered));
//            }
//        }
//
//        return false;
//    }
//
//    public boolean isFullNameValid(TextInputLayout fullNameTextInputLayout, String newFullName) {
//        boolean fullNameEntered = !TextUtils.isEmpty(newFullName.trim());
//        boolean valid = true;
//
//        if (fullNameEntered) {
//            if (newFullName.length() < FULL_NAME_MIN_LENGTH) {
//                valid = false;
//                fullNameTextInputLayout.setError(context.getString(R.string.auth_full_name_field_is_too_short));
//            } else if (newFullName.length() > FULL_NAME_MAX_LENGTH) {
//                valid = false;
//                fullNameTextInputLayout.setError(context.getString(R.string.auth_full_name_field_is_too_long));
//            }
//        } else {
//            valid = false;
//            fullNameTextInputLayout.setError(context.getString(R.string.profile_full_name_not_entered));
//        }
//
//        return valid;
//    }
//
//    public boolean isForgotPasswordDataValid(TextInputLayout emailTextInputLayout, String email) {
//        boolean isEmailEntered = !TextUtils.isEmpty(email.trim());
//
//        if (isEmailEntered) {
//            if (!isEmailValid(email)) {
//                emailTextInputLayout.setError(context.getString(R.string.forgot_password_email_field_is_incorrect));
//            } else {
//                return true;
//            }
//        } else {
//            emailTextInputLayout.setError(context.getString(R.string.forgot_password_email_field_not_entered));
//        }
//
//        return false;
//    }
//
//    public static boolean isNull(String value) {
//        return value == null || value.equals(NULL);
//    }
//
//    private static boolean isValidMaxLengthName(File file) {
//        return file.getName().length() < ConstsCore.MAX_FILENAME_LENGTH;
//    }
//
//    private static boolean isValidMaxImageSize(File file) {
//        return file.length() < ConstsCore.MAX_IMAGE_SIZE;
//    }
//
//    private static boolean isValidMaxAudioVideoSize(File file) {
//        return file.length() < ConstsCore.MAX_AUDIO_VIDEO_SIZE;
//    }
//
//    private static boolean isValidMinAudioDuration(File file) {
//        int duration = MediaUtils.getMetaData(file.getPath()).durationSec();
//        return duration >= ConstsCore.MIN_RECORD_DURATION_IN_SEC;
//    }
//
//
//    private static boolean isValidFileType(String mimeType) {
//        String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
//        return isValidMimeType(mimeType) || isValidExtension(extension);
//    }
//
//    private boolean isEmailValid(String email) {
//        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
//        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
//    }

}