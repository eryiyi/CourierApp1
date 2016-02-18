<?php
	header("Content-type: text/html; charset=utf-8"); 
	require_once("../inc/common.inc.php");
	require_once("appdata.fun.php");
	require_once("appconst.inc.php");
	//$_POST["code"] = "afb3a73817e688fb49991ed6a2e57649"; 
	$result = array("status"=>'',"user_id"=>0);
	if($_POST["code"]==md5(MD5_OPEN_CODE))
	{
		$data = array();
		$data["user_name"] = $_POST["user_name"]?trim($_POST["user_name"]):'';
		$data["user_pass"] = $_POST["user_pass"]?trim($_POST["user_pass"]):'';
		//$data = array("user_name"=>"18792455303","user_pass"=>'12345678');
		$user_id = get_user_id($data);
		if($user_id)
		{
			$result["status"] = '000';
			$result["user_id"] = $user_id;
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