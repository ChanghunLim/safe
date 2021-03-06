/*******************************************************************************
    Copyright (c) 2013-2014, S-Core, KAIST.
    All rights reserved.

    Use is subject to license terms.

    This distribution may include materials developed by third parties.
  ******************************************************************************/

package kr.ac.kaist.jsaf.analysis.typing.models.Tizen

import kr.ac.kaist.jsaf.analysis.typing.AddressManager._


import kr.ac.kaist.jsaf.analysis.cfg.{CFG, CFGExpr, InternalError}
import kr.ac.kaist.jsaf.analysis.typing.domain.{BoolFalse => F, BoolTrue => T, _}
import kr.ac.kaist.jsaf.analysis.typing.models._
import kr.ac.kaist.jsaf.analysis.typing._

import kr.ac.kaist.jsaf.analysis.typing.domain.Context
import kr.ac.kaist.jsaf.analysis.typing.domain.Heap

object TIZENContactPhoneNumber extends Tizen {
  private val name = "ContactPhoneNumber"
  /* predefined locations */
  val loc_cons = newSystemRecentLoc(name + "Cons")
  val loc_proto = newSystemRecentLoc(name + "Proto")
  /* constructor or object*/
  private val prop_cons: List[(String, AbsProperty)] = List(
    ("@class", AbsConstValue(PropValue(AbsString.alpha("Function")))),
    ("@proto", AbsConstValue(PropValue(ObjectValue(Value(ObjProtoLoc), F, F, F)))),
    ("@extensible",                 AbsConstValue(PropValue(T))),
    ("@scope",                      AbsConstValue(PropValueNullTop)),
    ("@construct",               AbsInternalFunc("tizen.ContactPhoneNumber.constructor")),
    ("@hasinstance", AbsConstValue(PropValueNullTop)),
    ("prototype", AbsConstValue(PropValue(ObjectValue(Value(loc_proto), F, F, F))))
  )

  /* prototype */
  private val prop_proto: List[(String, AbsProperty)] = List(
    ("@class", AbsConstValue(PropValue(AbsString.alpha("CallbackObject")))),
    ("@proto", AbsConstValue(PropValue(ObjectValue(Value(ObjProtoLoc), F, F, F)))),
    ("@extensible", AbsConstValue(PropValue(T)))
  )

  override def getInitList(): List[(Loc, List[(String, AbsProperty)])] = List(
    (loc_cons, prop_cons), (loc_proto, prop_proto)
  )

  override def getSemanticMap(): Map[String, SemanticFun] = {
    Map(
      ("tizen.ContactPhoneNumber.constructor" -> (
        (sem: Semantics, h: Heap, ctx: Context, he: Heap, ctxe: Context, cp: ControlPoint, cfg: CFG, fun: String, args: CFGExpr) => {
          val lset_this = h(SinglePureLocalLoc)("@this")._2._2
          val lset_env = h(SinglePureLocalLoc)("@env")._2._2
          val set_addr = lset_env.foldLeft[Set[Address]](Set())((a, l) => a + locToAddr(l))
          if (set_addr.size > 1) throw new InternalError("API heap allocation: Size of env address is " + set_addr.size)
          val addr_env = (cp._1._1, set_addr.head)
          val addr1 = cfg.getAPIAddress(addr_env, 0)
          val l_r1 = addrToLoc(addr1, Recent)
          val (h_2, ctx_2) = Helper.Oldify(h, ctx, addr1)
          val v_1 = getArgValue(h_2, ctx_2, args, "0")
          val n_arglen = Operator.ToUInt32(getArgValue(h_2, ctx_2, args, "length"))

          val o_new = Obj.empty.
            update("@class", PropValue(AbsString.alpha("Object"))).
            update("@proto", PropValue(ObjectValue(Value(TIZENContactPhoneNumber.loc_proto), F, F, F))).
            update("@extensible", PropValue(T)).
            update("number", PropValue(ObjectValue(Value(Helper.toString(v_1._1)), T, T, T)))

          val (h_3, es_1) = AbsNumber.getUIntSingle(n_arglen) match {
            case Some(n) if n <= 1 =>
              val o_arr = Helper.NewArrayObject(AbsNumber.alpha(1))
              val o_arr1 = o_arr.update("0", PropValue(ObjectValue(Value(AbsString.alpha("VOICE")), T, T, T)))
              val h_3 = h_2.update(l_r1, o_arr1)
              val o_new2 = o_new.
                update("isDefault", PropValue(ObjectValue(Value(F), T, T, T))).
                update("types", PropValue(ObjectValue(Value(l_r1), T, T, T)))
              val h_4 = lset_this.foldLeft(h_3)((_h, l) => _h.update(l, o_new2))
              (h_4, TizenHelper.TizenExceptionBot)
            case Some(n) if n == 2 =>
              val v_2 = getArgValue(h_2, ctx_2, args, "1")
              val es_1 =
                if (v_2._2.exists((l) => Helper.IsArray(h_2, l) <= F))
                  Set[WebAPIException](TypeMismatchError)
                else TizenHelper.TizenExceptionBot
              val o_arr = Helper.NewArrayObject(AbsNumber.alpha(1))
              val o_arr1 = o_arr.update("0", PropValue(ObjectValue(Value(AbsString.alpha("VOICE")), T, T, T)))
              val obj = v_2._2.foldLeft(o_arr1)((_o, ll) => {
                val n_length = Operator.ToUInt32(Helper.Proto(h_2, ll, AbsString.alpha("length")))
                val obj_1 = n_length.getAbsCase match {
                  case AbsBot => _o
                  case _ => AbsNumber.getUIntSingle(n_length) match {
                    case Some(n) => {
                      val (o__, cnt) = (0 until n.toInt).foldLeft((_o, 0))((__o, i) => {
                        val vi = Helper.Proto(h_2, ll, AbsString.alpha(i.toString))
                        val o_ =
                          if (vi._1._5 == AbsString.alpha("WORK") || vi._1._5 == AbsString.alpha("HOME") ||
                              vi._1._5 == AbsString.alpha("VOICE") || vi._1._5 == AbsString.alpha("FAX") ||
                              vi._1._5 == AbsString.alpha("MSG") || vi._1._5 == AbsString.alpha("CELL") ||
                              vi._1._5 == AbsString.alpha("PAGER") || vi._1._5 == AbsString.alpha("BBS") ||
                              vi._1._5 == AbsString.alpha("MODEM") || vi._1._5 == AbsString.alpha("CAR") ||
                              vi._1._5 == AbsString.alpha("ISDN") || vi._1._5 == AbsString.alpha("VIDEO") ||
                              vi._1._5 == AbsString.alpha("PCS")){
                            val o = __o._1.update(__o._2.toString(), PropValue(ObjectValue(Value(vi._1._5), T, T, T)))
                            (o, __o._2 + 1)
                          }
                          else __o
                        o_
                      })
                      o__
                    }
                    case _ => {
                      val vi = Helper.Proto(h_2, ll, AbsString.alpha(Str_default_number))
                      val o_ =
                        if (vi._1._5 == AbsString.alpha("WORK") || vi._1._5 == AbsString.alpha("HOME") ||
                            vi._1._5 == AbsString.alpha("VOICE") || vi._1._5 == AbsString.alpha("FAX") ||
                            vi._1._5 == AbsString.alpha("MSG") || vi._1._5 == AbsString.alpha("CELL") ||
                            vi._1._5 == AbsString.alpha("PAGER") || vi._1._5 == AbsString.alpha("BBS") ||
                            vi._1._5 == AbsString.alpha("MODEM") || vi._1._5 == AbsString.alpha("CAR") ||
                            vi._1._5 == AbsString.alpha("ISDN") || vi._1._5 == AbsString.alpha("VIDEO") ||
                            vi._1._5 == AbsString.alpha("PCS")){
                          _o.update(Str_default_number, PropValue(ObjectValue(Value(vi._1._5), T, T, T)))
                        }
                        else _o
                      o_
                    }
                  }
                }
                obj_1
              })
              val h_3 = h_2.update(l_r1, obj)
              val o_new2 = o_new.
                update("isDefault", PropValue(ObjectValue(Value(F), T, T, T))).
                update("types", PropValue(ObjectValue(Value(l_r1), T, T, T)))
              val h_4 = lset_this.foldLeft(h_3)((_h, l) => _h.update(l, o_new2))
              (h_4, es_1)
            case _ =>
              val v_2 = getArgValue(h_2, ctx_2, args, "1")
              val v_3 = getArgValue(h_2, ctx_2, args, "2")
              val es_1 =
                if (v_2._2.exists((l) => Helper.IsArray(h_2, l) <= F))
                  Set[WebAPIException](TypeMismatchError)
                else TizenHelper.TizenExceptionBot
              val es_2 =
                if (v_3._1._3 </ BoolTop)
                  Set[WebAPIException](TypeMismatchError)
                else TizenHelper.TizenExceptionBot
              val o_arr = Helper.NewArrayObject(AbsNumber.alpha(1))
              val o_arr1 = o_arr.update("0", PropValue(ObjectValue(Value(AbsString.alpha("VOICE")), T, T, T)))
              val obj = v_2._2.foldLeft(o_arr1)((_o, ll) => {
                val n_length = Operator.ToUInt32(Helper.Proto(h_2, ll, AbsString.alpha("length")))
                val obj_1 = n_length.getAbsCase match {
                  case AbsBot => _o
                  case _ => AbsNumber.getUIntSingle(n_length) match {
                    case Some(n) => {
                      val (o__, cnt) = (0 until n.toInt).foldLeft((_o, 0))((__o, i) => {
                        val vi = Helper.Proto(h_2, ll, AbsString.alpha(i.toString))
                        val o_ =
                          if (vi._1._5 == AbsString.alpha("WORK") || vi._1._5 == AbsString.alpha("HOME") ||
                              vi._1._5 == AbsString.alpha("VOICE") || vi._1._5 == AbsString.alpha("FAX") ||
                              vi._1._5 == AbsString.alpha("MSG") || vi._1._5 == AbsString.alpha("CELL") ||
                              vi._1._5 == AbsString.alpha("PAGER") || vi._1._5 == AbsString.alpha("BBS") ||
                              vi._1._5 == AbsString.alpha("MODEM") || vi._1._5 == AbsString.alpha("CAR") ||
                              vi._1._5 == AbsString.alpha("ISDN") || vi._1._5 == AbsString.alpha("VIDEO") ||
                              vi._1._5 == AbsString.alpha("PCS")){
                            val o = __o._1.
                              update("length", PropValue(ObjectValue(Value(AbsNumber.alpha(__o._2+1)), T, T, T))).
                              update(__o._2.toString(), PropValue(ObjectValue(Value(vi._1._5), T, T, T)))
                            (o, __o._2 + 1)
                          }
                          else __o
                        o_
                      })
                      o__
                    }
                    case _ => {
                      val vi = Helper.Proto(h_2, ll, AbsString.alpha(Str_default_number))
                      val o_ =
                        if (vi._1._5 == AbsString.alpha("WORK") || vi._1._5 == AbsString.alpha("HOME") ||
                            vi._1._5 == AbsString.alpha("VOICE") || vi._1._5 == AbsString.alpha("FAX") ||
                            vi._1._5 == AbsString.alpha("MSG") || vi._1._5 == AbsString.alpha("CELL") ||
                            vi._1._5 == AbsString.alpha("PAGER") || vi._1._5 == AbsString.alpha("BBS") ||
                            vi._1._5 == AbsString.alpha("MODEM") || vi._1._5 == AbsString.alpha("CAR") ||
                            vi._1._5 == AbsString.alpha("ISDN") || vi._1._5 == AbsString.alpha("VIDEO") ||
                            vi._1._5 == AbsString.alpha("PCS")){
                          _o.update(Str_default_number, PropValue(ObjectValue(Value(vi._1._5), T, T, T)))
                        }
                        else _o
                      o_
                    }
                  }
                }
                obj_1
              })
              val h_3 = h_2.update(l_r1, obj)
              val o_new2 = o_new.
                update("isDefault", PropValue(ObjectValue(Value(v_3._1._3), T, T, T))).
                update("types", PropValue(ObjectValue(Value(l_r1), T, T, T)))
              val h_4 = lset_this.foldLeft(h_3)((_h, l) => _h.update(l, o_new2))
              (h_4, es_1 ++ es_2)
          }
          val (h_e, ctx_e) = TizenHelper.TizenRaiseException(h, ctx, es_1)
          ((Helper.ReturnStore(h_3, Value(lset_this)), ctx_2), (he + h_e, ctxe + ctx_e))
        }
        ))
    )
  }

  override def getPreSemanticMap(): Map[String, SemanticFun] = {Map()}
  override def getDefMap(): Map[String, AccessFun] = {Map()}
  override def getUseMap(): Map[String, AccessFun] = {Map()}
}
