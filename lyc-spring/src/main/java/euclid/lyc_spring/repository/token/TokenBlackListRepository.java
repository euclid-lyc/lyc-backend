package euclid.lyc_spring.repository.token;

public interface TokenBlackListRepository {

    public void addTokenToBlackList(String accessToken);

    public boolean isBlackListed(String accessToken);
}
