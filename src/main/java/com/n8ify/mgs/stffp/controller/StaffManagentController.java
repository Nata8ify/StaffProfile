package com.n8ify.mgs.stffp.controller;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.n8ify.mgs.stffp.dealer.StaffManager;
import com.n8ify.mgs.stffp.model.Staff;
import com.n8ify.mgs.stffp.utils.Generator;

@Controller
public class StaffManagentController {

	private static final Logger logger = LoggerFactory.getLogger(StaffManagentController.class);

	@Autowired
	private StaffManager staffManager;

	@Autowired
	private CommonsMultipartResolver multipartResolver;

	private final String PORTRAIT_DIR = "/resources/portraits/";

	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String toManage() {
		// HttpServletRequest request
		// logger.info(((Staff)(request.getSession(false).getAttribute("thisStaff"))).toString());
		return "manage/manage";
	}

	@RequestMapping(value = "/insertPerson", method = RequestMethod.POST)
	public String insertPerson(Model model, @RequestParam(value = "staffId", required = true) String staffId,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "tel", required = true) String tel,
			@RequestParam(value = "division", required = true) String division,
			@RequestParam(value = "position", required = true) String position,
			@RequestParam(value = "protraitPath", required = false) String protraitPath,
			@RequestParam(value = "hostManagerId", required = false) String hostManagerId,
			@RequestParam(value = "password", required = false, defaultValue = "") String password,
			@RequestParam(value = "insertType", required = true) String insertType) {
		// Checking Is this an Administrator Account Roll.
		switch (insertType) {
		case Staff.TYPE_STAFF:
			if (staffManager.insertStaff(
					new Staff(staffId, name, email, tel, division, position, protraitPath, hostManagerId, gender,
							Staff.TYPE_STAFF),
					password.equals("") ? Generator.getInstance().genPassword() : password)) {
				model.addAttribute("msg", "สำเร็จ!");
			} else {
				model.addAttribute("msg", "ไม่สำเร็จ!");
			}
			break;
		case Staff.TYPE_MANAGER:
			break;
		case Staff.TYPE_ADMINISTRATOR:
			break;
		default: // TODO
		}
		return toManage();
	}

	@RequestMapping(value = "/editPerson", method = RequestMethod.POST)
	public String editPerson(Model model, @RequestParam(value = "staffId", required = true) String staffId,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "tel", required = true) String tel,
			@RequestParam(value = "division", required = true) String division,
			@RequestParam(value = "position", required = true) String position,
			@RequestParam(value = "protraitPath", required = false) String protraitPath,
			@RequestParam(value = "hostManagerId", required = false) String hostManagerId,
			@RequestParam(value = "editType", required = true) String editType) {
		switch (editType) {
		case Staff.TYPE_STAFF:
			if (staffManager.editStaff(new Staff(staffId, name, email, tel, division, position, protraitPath,
					hostManagerId, gender, Staff.TYPE_STAFF))) {
				model.addAttribute("msg", "สำเร็จ!");
			} else {
				model.addAttribute("msg", "ไม่สำเร็จ!");
			}
			break;
		case Staff.TYPE_MANAGER:
			break;
		case Staff.TYPE_ADMINISTRATOR:
			break;
		default: // TODO
		}
		return toManage();
	}

	@RequestMapping(value = "/deletePerson", method = RequestMethod.POST)
	public String insertPerson(Model model, @RequestParam(value = "staffId", required = true) String staffId,
			@RequestParam(value = "deleteType", required = true) String deleteType) {
		// Checking Is this an Administrator Account Roll.
		switch (deleteType) {
		case Staff.TYPE_STAFF:
			if (staffManager.deleteStaffById(staffId)) {
				model.addAttribute("msg", "สำเร็จ!");
			} else {
				model.addAttribute("msg", "ไม่สำเร็จ!");
			}
			break;
		case Staff.TYPE_MANAGER:
			break;
		case Staff.TYPE_ADMINISTRATOR:
			break;
		default: // TODO
		}
		return toManage();
	}

	@RequestMapping(value = "/editSelf", method = RequestMethod.POST)
	public String editSelf(Model model, HttpServletRequest request,
			@RequestParam(value = "staffId", required = true) String staffId,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "tel", required = true) String tel,
			@RequestParam(value = "protraitPath", required = false) String protraitPath,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "editType", required = false) String editType) {

		if (staffManager.editSelfStaff(
				new Staff(staffId, name, email, tel, protraitPath.equals("") ? null : protraitPath), password)) {
			logger.info("UPDATED");
			// HttpSession session;
			// session = request.getSession(false);
			// session.removeAttribute("thisStaff");
			// session.removeAttribute("thisPassword");
			// Staff.setStaffInstance(staffManager.getStaffById(staffId));
			// session.setAttribute("thisStaff", Staff.getStaffInstance());
			// StaffAccess.setAccessInstance(new StaffAccess(staffId,
			// password));
			// session.setAttribute("thisPassword",
			// StaffAccess.getAccessInstance());
			Resource res = new ClassPathResource("");
			File oldImg = new File(request.getSession().getServletContext()
					.getRealPath("/resources/portraits/" + Staff.getStaffInstance().getProtraitPath()));
			if (!oldImg.delete()) {
				logger.error(staffId + ": IMG NOT DEL : " + " : Name " + oldImg.getName() + " : is file : "
						+ oldImg.isFile() + "::::" + oldImg.getPath());
			} else {
				logger.error(staffId + ": IMG DELED : " + " : Name " + oldImg.getName() + " : is file : "
						+ oldImg.isFile() + "::::" + oldImg.getPath());
			}
			return "redirect:login?staffId=" + staffId + "&password=" + password;
		}
		logger.info("NO UPDATE");
		return "home";
	}

	@RequestMapping(value = "/editSelf.f", method = RequestMethod.POST, headers = "content-type=multipart/*")
	public String editSelfWithPortrait(Model model, HttpServletRequest request,
			@RequestParam(value = "protraitPath") MultipartFile img) throws IllegalStateException, IOException {
		logger.info("IS MULT : " + multipartResolver.isMultipart(request));
		MultipartHttpServletRequest mrequest = multipartResolver.resolveMultipart(request);
		String imgName = img.isEmpty() ? Staff.getStaffInstance().getProtraitPath()
				: Generator.getInstance().genImageName(img.getOriginalFilename());
		if (!img.isEmpty())
			img.transferTo(new File(mrequest.getRealPath(PORTRAIT_DIR) + imgName));

		if (staffManager.editSelfStaff(
				new Staff(mrequest.getParameter("staffId"), mrequest.getParameter("name"),
						mrequest.getParameter("email"), mrequest.getParameter("tel"), imgName),
				mrequest.getParameter("password"))) {
			logger.info("UPDATED");//
			if ((!img.isEmpty())) {
				File oldImg = new File(mrequest.getRealPath(PORTRAIT_DIR + Staff.getStaffInstance().getProtraitPath()));
				if (!oldImg.delete()) {
					logger.error(": IMG NOT DEL : " + " : Name " + oldImg.getName() + " : is file : " + oldImg.isFile()
							+ "::::" + oldImg.getPath());
				} else {
					logger.error(": IMG DELED : " + " : Name " + oldImg.getName() + " : is file : " + oldImg.isFile()
							+ "::::" + oldImg.getPath());
				}
			}
			return "redirect:login?staffId=" + mrequest.getParameter("staffId") + "&password="
					+ mrequest.getParameter("password");

		}

		return "home";
	}

	// private String imageVerify(MultipartFile img, MultipartHttpServletRequest
	// mrequest) throws IllegalStateException, IOException{
	// if(img.isEmpty())return "";
	// File oldImg = new File(mrequest.getRealPath(PORTRAIT_DIR +
	// Staff.getStaffInstance().getProtraitPath()));
	// if (!oldImg.delete()) {
	// logger.error(": IMG NOT DEL : " + " : Name " + oldImg.getName() + " : is
	// file : "
	// + oldImg.isFile() + "::::" + oldImg.getPath());
	// } else {
	// logger.error(": IMG DELED : " + " : Name " + oldImg.getName() + " : is
	// file : "
	// + oldImg.isFile() + "::::" + oldImg.getPath());
	// }
	// return Generator.getInstance().genImageName(img.getOriginalFilename());
	// }
}
