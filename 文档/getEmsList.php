<?php
	header("Content-type: text/html; charset=utf-8"); 
	require_once("../inc/common.inc.php");
	require_once("appdata.fun.php");
	require_once("appconst.inc.php");
	$_POST["code"] = "afb3a73817e688fb49991ed6a2e57649"; 
	$result = array("status"=>'',"datalist"=>array());
	if($_POST["code"]==md5(MD5_OPEN_CODE))
	{
		$data = array();
		$user_id = $_POST["user_id"]?trim($_POST["user_id"]):1;
		$status = $_POST["status"]?trim($_POST["status"]):0;
		$pageszie = $_POST["pageszie"]?trim($_POST["pageszie"]):0;		//每页显示条数
		$page = $_POST["page"]?trim($_POST["page"]):0;					//页码
		$type = $_POST["type"]?$_POST["type"]:1;						//快件类型 1-收件 2-发件
		if($user_id)
		{
			$result["status"] = '000';
			$result["datalist"] = urlencode(get_ems_list($user_id,$status,$type,$pagesize,$page));
			//$result["datalist"] = get_ems_list(3552,1,1);
		}
		else
		{
			$result["status"] = '011';
			$result["user_id"] = 0;
		}
	}
	else
	{
		$result["status"] = '091';
	}
	echo json_encode($result);
	
?>