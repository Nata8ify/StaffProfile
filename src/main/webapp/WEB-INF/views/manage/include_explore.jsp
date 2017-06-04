
<div class="col-md-12 panel-default">
	<div class="content-box-header panel-heading">
		<div class="panel-title ">Staff Explorer</div>
		<!-- 							<div class="panel-options">
								<a href="#" data-rel="collapse"><i
									class="glyphicon glyphicon-refresh"></i></a> <a href="#"
									data-rel="reload"><i class="glyphicon glyphicon-cog"></i></a>
							</div> -->
	</div>
	<div class="content-box-large box-with-header">
		<table id='staff-explore-table'>
			<thead>
				<tr>
					<th></th>
					<th>StaffId</th>
					<th>Name</th>
					<th>E-mail</th>
					<th>Tel</th>
					<th>Division</th>
					<th>Position</th>
					<th>Edit</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		<br /> <br />
	</div>
</div>
<script
	src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"
></script>
<script type="text/javascript">
	var staffTable;
	$(document)
			.ready(
					function() {
						staffTable = $('#staff-explore-table')
								.DataTable(
										{
											"ajax" : {
												"url" : "${pageContext.request.contextPath}/viewAll?json=true",
												"dataSrc" : ""
											},
											"columns" : [ {
												"data" : "protraitPath",
												"width" : "10%"
											}, {
												"data" : "staffId",
												"width" : "5%"
											}, {
												"data" : "name",
												"width" : "20%"
											}, {
												"data" : "email",
												"width" : "10%"
											}, {
												"data" : "tel",
												"width" : "10%"
											}, {
												"data" : "division",
												"width" : "10%"
											}, {
												"data" : "position",
												"width" : "20%"
											}, {
												"width" : "5%"
											} ],
											"columnDefs" : [
													{
														"targets" : 0,
														"data" : "protraitPath",
														"searchable" : false,
														"render" : function(
																url, type, row,
																meta) {
															var imgSource = row.protraitPath == null ? "${pageContext.request.contextPath}/resources/portraits/noimg.png"
																	: "${pageContext.request.contextPath}/resources/portraits/"
																			+ row.protraitPath;
															return '<img src="'+imgSource+'" width="100px" alt="Portrait"/>';
														}
													},
													{
														"targets" : -1,
														"data" : "",
														"searchable" : false,
														"defaultContent" : "<button class='staff-edit btn btn-default'><i class='glyphicon glyphicon-pencil'></i></button>"
													}

											],
											"order" : [ [ 1, "asc" ] ]
										});
					});
</script>
<script type="text/javascript">
	/* modal_editfull */
	var staffList;
	var staffs;
	var unassignedStaffs;
	$(document).ready(function() {
		$.when($.ajax({
			"url" : "${pageContext.request.contextPath}/viewAll?json=true"
		})).then(function(json) {
			staffList = $.parseJSON(json);
		});

		$.ajax({
			"url" : "viewAllUnassignedStaffs?json=true",
			"success" : function(uslist) {
				unassignedStaffs = $.parseJSON(uslist);
			}
		});
		$.ajax({
			"url" : "viewAllStaffs?json=true",
			"success" : function(slist) {
				unassignedStaffs = $.parseJSON(slist);
			}
		});
		var managers;
		$.ajax({
			"url" : "viewAllMngs?json=true",
			"success" : function(mlist) {
				managers = $.parseJSON(mlist);
			}
		});
	});
	$('#staff-explore-table tbody')
			.on(
					'click',
					'tr .staff-edit',
					function() {
						var thisStaffId = staffTable.row($(this).parents('tr'))
								.data().staffId;
						$
								.each(
										staffList,
										function(index, value) {
											if (value.staffId == thisStaffId) {
												$('#editType').val(
														value.staffType);
												$('#staffId')
														.val(value.staffId);
												$('#name').val(value.name);
												$('#email').val(value.email);
												$('#tel').val(value.tel);
												$('#division').val(
														value.division);
												$('#position').val(
														value.position);
												$('#gender').val(value.gender);
												$('#hostManagerId').val(
														value.hostManagerId);
												$('#protraitPathOld').val(
														value.protraitPath);
												$('#hostManagerName-show')
														.val(
																value.hostManagerName != null ? value.hostManagerName
																		: "Unassigned");
												console.log(value);
											}
										})
						$('#modal-staff-edit').modal();
					});
</script>
<script>
	/* modal_pickmng */
	var managers;
	$.ajax({
		"url" : "viewAllMngs?json=true",
		"success" : function(mlist) {
			managers = $.parseJSON(mlist);
		}
	});
	$('#btn-assign-mng')
			.click(
					function() {

						$('#mnglist-tbody').html("");
						$
								.each(
										managers,
										function(index, value) {
											var staffId = value.staffId;
											var name = value.name;
											var position = value.position;
											var btnAssignHtml = $("<tr><td>"
													+ staffId
													+ "</td><td>"
													+ name
													+ "</td><td>"
													+ position
													+ "</td><td><button class='btn btn-default .btn-assign-mng'><i class='glyphicon glyphicon-thumbs-up'></i></button></td></tr>");
											$('#mnglist-tbody')
													.append(
															btnAssignHtml
																	.click(function() {
																		$(
																				'#hostManagerId')
																				.val(
																						staffId);
																		$(
																				'#hostManagerName-show')
																				.val(
																						name);
																		$(
																				'#opt-staffid')
																				.val(
																						"Staff (assign to "
																								+ name
																								+ ")");
																		$(
																				'#modal-assign-mng')
																				.modal(
																						'hide');
																	}));
										});
						$('#modal-assign-mng').modal();

					});
</script>