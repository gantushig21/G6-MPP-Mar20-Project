package business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController implements ControllerInterface {
	public static Auth currentAuth = null;
	
	public void login(String id, String password) throws LoginException {
		DataAccess da = new DataAccessFacade();
		HashMap<String, User> map = da.readUserMap();
		if(!map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if(!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		currentAuth = map.get(id).getAuthorization();
		
	}
	@Override
	public List<String> allMemberIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readMemberMap().keySet());
		return retval;
	}
	
	@Override
	public List<String> allBookIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().keySet());
		return retval;
	}
	
	@Override
	public List<LibraryMember> allMembers() {
		DataAccess da = new DataAccessFacade();
		
		Collection<LibraryMember> members = da.readMemberMap().values();
		
		
		return new ArrayList<LibraryMember>(members);
	}
	
	@Override
	public void addMember(LibraryMember member) throws AlreadyExistException {
		DataAccess da = new DataAccessFacade();
		
		boolean result = da.saveNewMember(member);		
		
		if (!result)
			throw new AlreadyExistException("The member with this ID is already exists!");
	}
	@Override
	public void deleteMember(String memberId) {
		DataAccess da = new DataAccessFacade();
		
		da.deleteMember(memberId);				
	}
	@Override
	public void updateMember(LibraryMember member) {
		DataAccess da = new DataAccessFacade();
		
		da.updateMember(member);				
	}	
}
