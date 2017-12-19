package com.example.manueli.siscam;

import android.provider.Settings;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by integra-des2 on 16/12/2017.
 */

public class obtenerverificarasignacionruta {

    Conexion  Conexion = new Conexion();
    PreparedStatement stmt;
    ResultSet rs;

    protected JSONObject obtenerRutas(){
        JSONArray arrayjson = new JSONArray();
        JSONObject jsonObject;
        JSONObject json = new JSONObject();

        try {
            Connection con = Conexion.CONN();

            if (con != null) {

                String query2 = "declare @Inicio time(0),@Final time(0); set @Inicio =(select Convert(varchar(10),RIGHT( CONVERT(DATETIME, dateadd(hour,-1,HoraInicio), 108),8)) from CatTurnosCentral where id=1)" +
                        " set @Final=(select Convert(varchar(10),RIGHT( CONVERT(DATETIME, dateadd(hour,4,HoraFinal), 108),8)) as Termina from CatTurnosCentral where id=1) declare @TablaTemporal "+
                        " Table(CodEmpresa int,CodRuta int,Empresa varchar(50),Ruta varchar(100),LInicia varchar(50),Termina varchar(50),HoraInicio varchar(30),Tipo varchar(20),Dia int,CodUnidad varchar(20), "+
                        " Unidad varchar(10), CodChofer int,Chofer varchar(30),HoraIni varchar(20),HoraFinal varchar(20),Inicia varchar(50),Fecha varchar(15),Estatus bit,FechaHora datetime,Repetido int) "+
                        " if (@Inicio > @Final) begin insert into @TablaTemporal select G.CodEmpresa,G.CodRuta,E.NomComercial as Empresa,R.NomCorto as Ruta,R.LugarSalida as LInicia,R.LugarDestino as Termina,"+
                        " case when G.Tipo=1 then G.HoraInicio else G.HoraInicio + ' - ' + G.HoraFinal end as HoraInicio, case when G.Tipo=1 then 'SALIDA' else 'ENTRADA' end as Tipo,G.Dia,G.CodUnidad,U.Clave"+
                        "  as Unidad,G.CodChofer,C.Apodo as Chofer,G.HoraInicio,G.HoraFinal, case when G.Tipo=1 then '' else G.HoraInicio + ' - ' + R.LugarSalida end as Inicia,Convert(date,G.Fecha),G.Estatus,G.Fecha,0"+
                        "   from MqRutasDiariasGeneradas G inner join MqCatUbicaciones R on G.CodRuta=R.Codigo inner join CatEmpresasClientes E on E.Codigo=R.CodEmpresa inner join MqCatMaqyEquipo U on "+
                        "   U.Codigo=G.CodUnidad inner join CatPersonal C on C.CodPersonal=G.CodChofer where G.Dia=datepart(dw,GetDate()) and R.Estatus=1 and G.CodEmpresa=24 and CONVERT(VARCHAR(8),G.Fecha,108) "+
                        "   between @Inicio and '24:59:59' insert into @TablaTemporal select G.CodEmpresa,G.CodRuta,E.NomComercial as Empresa,R.NomCorto as Ruta,R.LugarSalida as LInicia,R.LugarDestino as Termina,case"+
                        "    when G.Tipo=1 then G.HoraInicio else G.HoraInicio + ' - ' + G.HoraFinal end as HoraInicio,case when G.Tipo=1 then 'SALIDA' else 'ENTRADA' end as Tipo,G.Dia,G.CodUnidad,U.Clave as Unidad,G.CodChofer,"+
                        "	C.Apodo as Chofer,G.HoraInicio,G.HoraFinal,case when G.Tipo=1 then '' else G.HoraInicio + ' - ' + R.LugarSalida end as Inicia,Convert(date,G.Fecha),G.Estatus,G.Fecha,0 "+
                        "	from MqRutasDiariasGeneradas G inner join MqCatUbicaciones R on G.CodRuta=R.Codigo inner join CatEmpresasClientes E on E.Codigo=R.CodEmpresa inner join MqCatMaqyEquipo U "+
                        "	on U.Codigo=G.CodUnidad inner join CatPersonal C on C.CodPersonal=G.CodChofer where G.Dia=datepart(dw,dateadd(day,1,getdate())) and R.Estatus=1 and G.CodEmpresa=24 and "+
                        "	CONVERT(VARCHAR(8),G.Fecha,108) between '00:00:00' and @Final end else begin insert into @TablaTemporal select G.CodEmpresa,G.CodRuta,E.NomComercial as Empresa,R.NomCorto as" +
                        "	Ruta,R.LugarSalida as LInicia,R.LugarDestino as Termina,case when G.Tipo=1 then G.HoraInicio else G.HoraInicio + ' - ' + G.HoraFinal end as HoraInicio,case when G.Tipo=1 "+
                        "	then 'SALIDA' else 'ENTRADA' end as Tipo,G.Dia,G.CodUnidad,U.Clave as Unidad,G.CodChofer,C.Apodo as Chofer,G.HoraInicio,G.HoraFinal,case when G.Tipo=1 then '' "+
                        "	else G.HoraInicio + ' - ' + R.LugarSalida end as Inicia,Convert(date,G.Fecha),G.Estatus,G.Fecha,0 from MqRutasDiariasGeneradas G inner join MqCatUbicaciones R on G.CodRuta=R.Codigo"+
                        "	 inner join CatEmpresasClientes E on E.Codigo=R.CodEmpresa inner join MqCatMaqyEquipo U on U.Codigo=G.CodUnidad inner join CatPersonal C on C.CodPersonal=G.CodChofer "+
                        "	 where G.Dia=datepart(dw,GetDate()) and R.Estatus=1 and G.CodEmpresa=24 and CONVERT(VARCHAR(8),Fecha,108) between @Inicio and @Final end SET NOCOUNT ON; "+
                        "	 DECLARE @CodRuta1 int,@CodUnidad1 int,@CodChofer1 int,@Dia1 int,@HoraIni1 varchar(20),@HoraFinal1 varchar(20) DECLARE vendor_cursor CURSOR FOR select "+
                        "	 CodRuta,CodUnidad,CodChofer,Dia,HoraIni,HoraFinal from @TablaTemporal OPEN vendor_cursor FETCH NEXT FROM vendor_cursor INTO @CodRuta1,@CodUnidad1,@CodChofer1,@Dia1,@HoraIni1,@HoraFinal1"+
                        "	  WHILE @@FETCH_STATUS = 0 begin if ((select count(*) from MqRutasDiariasGeneradas where CodRuta=@CodRuta1 and Dia=@Dia1 and HoraInicio=@HoraIni1 and HoraFinal=@HoraFinal1) > 1) "+
                        "	  begin update @TablaTemporal set Repetido=1 where CodRuta=@CodRuta1 and Dia=@Dia1 and HoraIni=@HoraIni1 and HoraFinal=@HoraFinal1 end if ((select count(*) from MqRutasDiariasGeneradas"+
                        "	   where CodUnidad=@CodUnidad1 and Dia=@Dia1 and HoraInicio=@HoraIni1 and HoraFinal=@HoraFinal1) > 1) begin update @TablaTemporal set Repetido=2 where CodUnidad=@CodUnidad1" +
                        "	   and Dia=@Dia1 and HoraIni=@HoraIni1 and HoraFinal=@HoraFinal1 end if ((select count(*) from MqRutasDiariasGeneradas where CodChofer=@CodChofer1 and Dia=@Dia1 and HoraInicio=@HoraIni1"+
                        "	    and HoraFinal=@HoraFinal1) > 1) begin update @TablaTemporal set Repetido=3 where CodChofer=@CodChofer1 and Dia=@Dia1 and HoraIni=@HoraIni1 and HoraFinal=@HoraFinal1"+
                        "		 end FETCH NEXT FROM vendor_cursor INTO @CodRuta1,@CodUnidad1,@CodChofer1,@Dia1,@HoraIni1,@HoraFinal1 end CLOSE vendor_cursor; DEALLOCATE vendor_cursor;"+
                        "		  select * from @TablaTemporal order by FechaHora,Ruta ";


                stmt = con.prepareStatement(query2);
                rs = stmt.executeQuery();


                while (rs.next()) {
                    jsonObject = new JSONObject();
                    jsonObject.put("CodRuta",rs.getString("CodRuta"));
                    jsonObject.put("Empresa",rs.getString("Empresa"));
                    arrayjson.put(jsonObject);
                }

                json.put ("datos",arrayjson);
            }

        }catch (Exception e) {
            System.out.print("error");
            e.printStackTrace();
        }

        return json;
    }

}
