import com.gaia.hermes.pushnotification.PushNoficationApi;
import com.gaia.hermes.pushnotification.WindowsPhonePNS;
import com.gaia.hermes.pushnotification.message.BaseToastMessage;

public class WindowsPhonePNSTest {
	public static void main(String[] args) {
		String token = "http://s.notify.live.net/u/1/hk2/H2QAAABmw_mZJUOfQ82oVYqXGR9DYNC7rwMKQDI3e6r_QvjzYNN-bd4OEFggYHOHh2gN0O73bTlMMj4LLRNF-N_ruVtheBMZa4ZSqqXDNG4wLL1mmtv_n__T-dAnMz_BCRoTCrw/d2luZG93c3Bob25lZGVmYXVsdA/6DfULbozYUipmXv1FLh_1g/jjF6Epo6IxHp3Q2WMQK9dNVnWkQ";
		PushNoficationApi api = new WindowsPhonePNS("ms-app://s-1-15-2-178685015-4227303602-993491238-2605550032-2075089136-3243607036-1120404382", "U8IMIleETNqDporApjQ1jvoNEtf7ieOv", false);
		api.push(token, new BaseToastMessage("hello", "WindowsPhone Test Application"));
	}
}
