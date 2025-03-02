package com.mbc.cook.controller;

import com.mbc.cook.dto.member.MemberDTO;
import com.mbc.cook.entity.member.MemberEntity;
import com.mbc.cook.service.member.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//현준
@Slf4j
@Controller
public class MemberController {
    @Autowired
    MemberService memberservice;

    @GetMapping(value = "/login")
    public String memberLogin(Model model) {
        model.addAttribute("cssPath", "/member/login");//css 패스 경로(바꾸지X)
        model.addAttribute("pageTitle", "로그인");//타이틀 제목
        return "member/login";
    }

    @GetMapping(value = "/admin/list")
    public String memberList(Model model, @RequestParam(required = false, defaultValue = "0", value = "page") int page ) {
        model.addAttribute("cssPath", "/member/list");//css 패스 경로(바꾸지X)
        model.addAttribute("pageTitle", "회원 관리");//타이틀 제목

        Page<MemberEntity> list = memberservice.UserControl(page);

        model.addAttribute("npage", page);
        model.addAttribute("total",list.getTotalPages());
        model.addAttribute("list", list.getContent());
        return "member/list";
    }

    @GetMapping(value = "/idsearch")
    public String idsearch(Model model) {
        model.addAttribute("cssPath", "/member/idsearch");//css 패스 경로(바꾸지X)
        model.addAttribute("pageTitle", "아이디 찾기");//타이틀 제목
        return "member/idsearch";
    }

    @GetMapping(value = "/pwsearch")
    public String pwsearch(Model model) {
        model.addAttribute("cssPath", "/member/pwsearch");//css 패스 경로(바꾸지X)
        model.addAttribute("pageTitle", "비밀번호 찾기");//타이틀 제목
        return "member/pwsearch";
    }

    @PostMapping(value = "/getid")
    public void idsearch(@RequestParam("name") String name, @RequestParam("tel") String tel, @RequestParam("email") String email, HttpServletResponse response) throws IOException {
        System.out.println("이름 "+name+" 전하번호"+tel+" 이메일 "+email);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter prw = response.getWriter();
        List<MemberEntity> UserID = memberservice.getid(name, tel, email);
        if(UserID==null || UserID.isEmpty()){
            prw.print("회원정보가 없습니다.");
        }
        else{
            for(MemberEntity member : UserID){
                prw.print("당신의 아이디는 "+member.getId()+" 입니다.");
            }
        }
    }

    @PostMapping(value = "/getpw")
    public void pwsearch(@RequestParam("id") String id, @RequestParam("name") String name, @RequestParam("tel") String tel, @RequestParam("email") String email, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter prw = response.getWriter();
        System.out.println(id+name+ tel+ email);
        int result = memberservice.getpw(id,name,tel,email);
        prw.print(result);
    }

    @GetMapping(value = "/pwupdate")
    public String pwupdate(@RequestParam("id") String id, Model mo){
        mo.addAttribute("cssPath", "/member/pwupdate");//css 패스 경로(바꾸지X)
        mo.addAttribute("pageTitle", "비밀번호 찾기");//타이틀 제목
        mo.addAttribute("id",id);
        return "/member/pwupdate";
    }

    @PostMapping(value = "/pwUpdate")
    public void pwUpdate(@RequestParam("id") String id, @RequestParam("pw") String pw, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter prw = response.getWriter();

        memberservice.pwupdate(id,pw);

        prw.print("비밀번호가 변경되었습니다.");
    }

}
