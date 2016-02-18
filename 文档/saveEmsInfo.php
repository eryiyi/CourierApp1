<?php
	header("Content-type: text/html; charset=utf-8"); 
	require_once("../inc/common.inc.php");
	require_once("appdata.fun.php");
	require_once("appconst.inc.php");
	$_POST["code"] = "afb3a73817e688fb49991ed6a2e57649"; 
	$result = array("status"=>'',"store_no"=>'');
	if($_POST["code"]==md5(MD5_OPEN_CODE))
	{
		$data = array();
		$data["user_id"] = $_POST["user_id"]?$_POST["user_id"]:0;
		$data["sign_no"] = $_POST["sign_no"]?trim($_POST["sign_no"]):'';									//订单号
		$data["phone"] = $_POST["phone"]?trim($_POST["phone"]):'';											//手机
		$data["company"] = $_POST["company"]?$_POST["company"]:'';											//快递公司
		$type = $_POST["type"]?$_POST["type"]:1;															//快件类型 1-收件 2-发件
		//$data = array("user_id"=>3552,"sign_no"=>'710024561112',"name"=>"小青蛙","phone"=>"18792455303","company"=>"申通");
		//$type = 1;		
		$result = save_ems_info($data,$type);
	}							
	else
	{
		$result["status"] = '091';
	}
	echo json_encode($result);
?>