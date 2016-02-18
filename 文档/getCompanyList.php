<?php
	header("Content-type: text/html; charset=utf-8"); 
	require_once("../inc/common.inc.php");
	require_once("appdata.fun.php");
	require_once("appconst.inc.php");
	$_POST["code"] = "afb3a73817e688fb49991ed6a2e57649"; 
	$result = array("status"=>'',"companys"=>array());
	if($_POST["code"]==md5(MD5_OPEN_CODE))
	{
		$companys = get_company_list();
		if($companys)
		{
			$result["status"] = '000';
			$result["companys"] = $companys;
		}
		else
		{
			$result["status"] = '011';
			$result["companys"] = array();
		}
	}
	else
	{
		$result["status"] = '091';
	}
	echo urldecode(json_encode($result));
?>