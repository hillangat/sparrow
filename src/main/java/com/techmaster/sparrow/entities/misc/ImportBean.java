package com.techmaster.sparrow.entities.misc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Workbook;

import javax.persistence.*;
import java.sql.Blob;


@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "IMPRT_BN")
public class ImportBean extends AuditInfoBean {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "IMPRT_ID", nullable = false)
	private Long importId;

	@Column(name = "ORGNL_FL_NAM", nullable = false)
	private String originalFileName;

	@Column(name = "BYT_LEN", nullable = false)
	private double byteLen;

	@Column(name = "BN_NAM", nullable = false)
	private String beanName;

	@Column(name = "STS", nullable = false)
	private String status;

	@Column(name = "TMP_LOC")
	private String tempLocation;

	@Transient
	private byte[] excelBytes;

	@Column(name = "EXCL_BLB")
	private Blob excelBlob;

	@Transient
	private Workbook workbook;

}
