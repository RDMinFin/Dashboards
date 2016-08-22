package shiro.utilities;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import dao.CUserDAO;
import pojo.CUser;
import utilities.CProperties;

public class CustomRealm extends JdbcRealm {

	public CustomRealm() {
		super();
		MysqlDataSource mysqlDS = new MysqlDataSource();
		mysqlDS.setURL(String.join("", "jdbc:mysql://", CProperties.getmemsql_host(), ":",
				String.valueOf(CProperties.getmemsql_port()), "/", CProperties.getmemsql_schema()));
		mysqlDS.setUser(CProperties.getmemsql_user());
		mysqlDS.setPassword(CProperties.getmemsql_password());
		this.setDataSource(mysqlDS);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
		final String username = userPassToken.getUsername();
		if (username == null)
			return null;

		CUser user = CUserDAO.getUser(username);
		if (user == null)
			return null;

		SaltedAuthenticationInfo info = new CustomSaltedAuthenticationInfo(username, user.getPassword(),
				user.getSalt());

		return info;
	}

	@Override
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		return CUserDAO.hasUserPermiso(principals.getPrimaryPrincipal().toString(), permission);
	}
}
