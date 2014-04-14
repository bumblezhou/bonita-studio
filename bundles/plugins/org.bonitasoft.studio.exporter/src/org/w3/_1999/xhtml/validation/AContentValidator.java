/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.w3._1999.xhtml.validation;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.w3._1999.xhtml.AbbrType;
import org.w3._1999.xhtml.AcronymType;
import org.w3._1999.xhtml.BType;
import org.w3._1999.xhtml.BdoType;
import org.w3._1999.xhtml.BigType;
import org.w3._1999.xhtml.BrType;
import org.w3._1999.xhtml.ButtonType;
import org.w3._1999.xhtml.CiteType;
import org.w3._1999.xhtml.CodeType;
import org.w3._1999.xhtml.DelType;
import org.w3._1999.xhtml.DfnType;
import org.w3._1999.xhtml.EmType;
import org.w3._1999.xhtml.IType;
import org.w3._1999.xhtml.ImgType;
import org.w3._1999.xhtml.InputType1;
import org.w3._1999.xhtml.InsType;
import org.w3._1999.xhtml.KbdType;
import org.w3._1999.xhtml.LabelType;
import org.w3._1999.xhtml.MapType;
import org.w3._1999.xhtml.ObjectType;
import org.w3._1999.xhtml.QType;
import org.w3._1999.xhtml.SampType;
import org.w3._1999.xhtml.ScriptType;
import org.w3._1999.xhtml.SelectType;
import org.w3._1999.xhtml.SmallType;
import org.w3._1999.xhtml.SpanType;
import org.w3._1999.xhtml.StrongType;
import org.w3._1999.xhtml.SubType;
import org.w3._1999.xhtml.SupType;
import org.w3._1999.xhtml.TextareaType;
import org.w3._1999.xhtml.TtType;
import org.w3._1999.xhtml.VarType;

/**
 * A sample validator interface for {@link org.w3._1999.xhtml.AContent}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface AContentValidator {
	boolean validate();

	boolean validateMixed(FeatureMap value);
	boolean validateGroup(FeatureMap value);
	boolean validateBr(EList<BrType> value);
	boolean validateSpan(EList<SpanType> value);
	boolean validateBdo(EList<BdoType> value);
	boolean validateMap(EList<MapType> value);
	boolean validateObject(EList<ObjectType> value);
	boolean validateImg(EList<ImgType> value);
	boolean validateTt(EList<TtType> value);
	boolean validateI(EList<IType> value);
	boolean validateB(EList<BType> value);
	boolean validateBig(EList<BigType> value);
	boolean validateSmall(EList<SmallType> value);
	boolean validateEm(EList<EmType> value);
	boolean validateStrong(EList<StrongType> value);
	boolean validateDfn(EList<DfnType> value);
	boolean validateCode(EList<CodeType> value);
	boolean validateQ(EList<QType> value);
	boolean validateSamp(EList<SampType> value);
	boolean validateKbd(EList<KbdType> value);
	boolean validateVar(EList<VarType> value);
	boolean validateCite(EList<CiteType> value);
	boolean validateAbbr(EList<AbbrType> value);
	boolean validateAcronym(EList<AcronymType> value);
	boolean validateSub(EList<SubType> value);
	boolean validateSup(EList<SupType> value);
	boolean validateInput(EList<InputType1> value);
	boolean validateSelect(EList<SelectType> value);
	boolean validateTextarea(EList<TextareaType> value);
	boolean validateLabel(EList<LabelType> value);
	boolean validateButton(EList<ButtonType> value);
	boolean validateIns(EList<InsType> value);
	boolean validateDel(EList<DelType> value);
	boolean validateScript(EList<ScriptType> value);
}
