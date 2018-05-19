package com.wedo.OMS.service;

import com.wedo.OMS.entity.Company;
import com.wedo.OMS.entity.User;
import com.wedo.OMS.repository.CompanyRepository;
import com.wedo.OMS.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private UserRepository userRepository;
    private CompanyRepository companyRepository;

    public CompanyServiceImpl(UserRepository userRepository,CompanyRepository companyRepository){
        this.userRepository = userRepository;
        this.companyRepository =companyRepository;
    }

    /**
     * 根据用户ID获取用户公司
     * @param userId
     * @return
     */
    @Override
    public Company getCompanyByUserId(Long userId) {
        User user = userRepository.findUserById(userId);
        return companyRepository.findCompanyById(user.getCompany().getId());
    }

    /**
     * 根据公司名搜索公司
     * @param
     * @return
     */
    @Override
    public List<Company> listCompaniesByCompanyname(String companyname) {
        return companyRepository.findCompaniesByNameContaining(companyname);
    }

    /**
     * 发包方根据公司ID获取公司的所有成员
     * @param companyId
     * @return
     */
    @Override
    public List<User> listCompanyUsersByCompanyId(Long companyId) {
        Company company = companyRepository.findCompanyById(companyId);
        return userRepository.findUsersByCompany(company);
    }

    /**
     * 队长根据名字搜索公司成员
     * @param username
     * @return
     */
    @Override
    public List<User> ListCompanyUsersByUsername(String username) {
        return userRepository.findUsersByNameContaining(username);
    }

    /**
     * 发包方新建公司
     * @param company
     * @return
     */
    @Override
    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

    /**
     * 发包方删除公司
     * @param companyId
     */
    @Override
    public void deleteCompanyByCompanyId(Long companyId) {
        companyRepository.deleteCompanyById(companyId);
    }

    /**
     * 删除公司成员
     * @param companyId
     * @param userId
     */
    @Override
    public void deleteCompanyUser(Long companyId, Long userId) {
        Company company= companyRepository.findCompanyById(companyId);
        userRepository.deleteUserByCompanyAndId(company,userId);
    }
}
