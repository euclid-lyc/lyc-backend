package euclid.lyc_spring.repository.mail;

public interface VerificationCodeRepository {

    public void addVerificationCode(String tempToken, String code);

    public void removeVerificationCode(String tempToken);

    String getVerificationCode(String tempToken);
}