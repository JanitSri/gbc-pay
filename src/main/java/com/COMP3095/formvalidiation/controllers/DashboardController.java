/**********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: The controller for all endpoints dealing with the dashboard (requires authentication for access).
 *********************************************************************************/

package com.COMP3095.formvalidiation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping({"/dashboard", "/dashboard.html"})
public class DashboardController {

    @GetMapping({"", "/index.html", "index"})
    public String getDetail(){
        return "dashboard/index";
    }

    @GetMapping({"tab1", "tab1.html"})
    public String getTab1(){
        return "dashboard/tab1";
    }

    @GetMapping({"tab2", "tab2.html"})
    public String getTab2(){
        return "dashboard/tab2";
    }

    @GetMapping({"tab3", "tab3.html"})
    public String getTab3(){
        return "dashboard/tab3";
    }

    @GetMapping({"tab4", "tab4.html"})
    public String getTab4(){
        return "dashboard/tab4";
    }
}
